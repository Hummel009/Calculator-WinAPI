package hummel

import com.sun.jna.Native
import com.sun.jna.Pointer
import com.sun.jna.platform.win32.WinDef.*
import com.sun.jna.platform.win32.WinUser.*
import kotlin.math.max
import kotlin.math.sqrt

const val WM_COMMAND: Int = 0x0111
const val WM_GETMINMAXINFO: Int = 0x0024

const val BUTTON_0_ID: Int = 0
const val BUTTON_1_ID: Int = 1
const val BUTTON_2_ID: Int = 2
const val BUTTON_3_ID: Int = 3
const val BUTTON_4_ID: Int = 4
const val BUTTON_5_ID: Int = 5
const val BUTTON_6_ID: Int = 6
const val BUTTON_7_ID: Int = 7
const val BUTTON_8_ID: Int = 8
const val BUTTON_9_ID: Int = 9
const val BUTTON_C_ID: Int = 10
const val BUTTON_DIVIDE_ID: Int = 11
const val BUTTON_DOT_ID: Int = 12
const val BUTTON_EQUALS_ID: Int = 13
const val BUTTON_E_ID: Int = 14
const val BUTTON_FACTORIAL_ID: Int = 15
const val BUTTON_INVERSE_ID: Int = 16
const val BUTTON_MINUS_ID: Int = 17
const val BUTTON_MULTIPLE_ID: Int = 18
const val BUTTON_PI_ID: Int = 19
const val BUTTON_PLUS_ID: Int = 20
const val BUTTON_SQUARE_ID: Int = 21
const val BUTTON_SQUARE_ROOT_ID: Int = 22
const val BUTTON_UNARY_MINUS_ID: Int = 23

lateinit var stack: MutableList<String>
lateinit var field: HWND

private val factorial: Array<Int> = arrayOf(1, 1, 2, 6, 24, 120, 720, 5040, 40320, 362880, 3628800, 39916800, 479001600)

fun main() {
	val windowClass = WNDCLASSEX()
	windowClass.lpfnWndProc = WndProc()
	val className = "HummelCalculator"
	windowClass.lpszClassName = className

	ExUser32.INSTANCE.RegisterClassEx(windowClass)

	val screenWidth = ExUser32.INSTANCE.GetSystemMetrics(SM_CXSCREEN)
	val screenHeight = ExUser32.INSTANCE.GetSystemMetrics(SM_CYSCREEN)

	val windowWidth = 260
	val windowHeight = 453

	val windowX = max(0.0, ((screenWidth - windowWidth) / 2).toDouble()).toInt()
	val windowY = max(0.0, ((screenHeight - windowHeight) / 2).toDouble()).toInt()

	val windowTitle = "WinAPI"
	val window = ExUser32.INSTANCE.CreateWindowEx(
		0,
		className,
		windowTitle,
		WS_OVERLAPPEDWINDOW,
		windowX,
		windowY,
		windowWidth,
		windowHeight,
		null,
		null,
		null,
		null
	)

	ExUser32.INSTANCE.ShowWindow(window, SW_SHOW)
	ExUser32.INSTANCE.UpdateWindow(window)

	val msg = MSG()
	while (ExUser32.INSTANCE.GetMessage(msg, null, 0, 0) != 0) {
		ExUser32.INSTANCE.TranslateMessage(msg)
		ExUser32.INSTANCE.DispatchMessage(msg)
	}
}

class WndProc : WindowProc {
	override fun callback(window: HWND, msg: Int, wParam: WPARAM, lParam: LPARAM): LRESULT {
		when (msg) {
			WM_CREATE -> {
				stack = ArrayList()

				field = registerField(window)

				registerButton(window, BUTTON_PI_ID, "π", 0, 0)
				registerButton(window, BUTTON_E_ID, "e", 1, 0)
				registerButton(window, BUTTON_C_ID, "C", 2, 0)
				registerButton(window, BUTTON_FACTORIAL_ID, "x!", 3, 0)
				registerButton(window, BUTTON_INVERSE_ID, "1/x", 0, 1)
				registerButton(window, BUTTON_SQUARE_ID, "x^2", 1, 1)
				registerButton(window, BUTTON_SQUARE_ROOT_ID, "√x", 2, 1)
				registerButton(window, BUTTON_DIVIDE_ID, "/", 3, 1)
				registerButton(window, BUTTON_7_ID, "7", 0, 2)
				registerButton(window, BUTTON_8_ID, "8", 1, 2)
				registerButton(window, BUTTON_9_ID, "9", 2, 2)
				registerButton(window, BUTTON_MULTIPLE_ID, "*", 3, 2)
				registerButton(window, BUTTON_4_ID, "4", 0, 3)
				registerButton(window, BUTTON_5_ID, "5", 1, 3)
				registerButton(window, BUTTON_6_ID, "6", 2, 3)
				registerButton(window, BUTTON_MINUS_ID, "-", 3, 3)
				registerButton(window, BUTTON_1_ID, "1", 0, 4)
				registerButton(window, BUTTON_2_ID, "2", 1, 4)
				registerButton(window, BUTTON_3_ID, "3", 2, 4)
				registerButton(window, BUTTON_PLUS_ID, "+", 3, 4)
				registerButton(window, BUTTON_UNARY_MINUS_ID, "-", 0, 5)
				registerButton(window, BUTTON_0_ID, "0", 1, 5)
				registerButton(window, BUTTON_DOT_ID, ".", 2, 5)
				registerButton(window, BUTTON_EQUALS_ID, "=", 3, 5)
			}

			WM_COMMAND -> {
				val buttonId = wParam.loword()

				when (buttonId) {
					BUTTON_0_ID -> pushSymbolWrapper(field, "0")
					BUTTON_1_ID -> pushSymbolWrapper(field, "1")
					BUTTON_2_ID -> pushSymbolWrapper(field, "2")
					BUTTON_3_ID -> pushSymbolWrapper(field, "3")
					BUTTON_4_ID -> pushSymbolWrapper(field, "4")
					BUTTON_5_ID -> pushSymbolWrapper(field, "5")
					BUTTON_6_ID -> pushSymbolWrapper(field, "6")
					BUTTON_7_ID -> pushSymbolWrapper(field, "7")
					BUTTON_8_ID -> pushSymbolWrapper(field, "8")
					BUTTON_9_ID -> pushSymbolWrapper(field, "9")

					BUTTON_E_ID -> pushSymbolWrapper(field, "2.72")
					BUTTON_PI_ID -> pushSymbolWrapper(field, "3.14")

					BUTTON_DOT_ID -> pushSymbolWrapper(field, ".")
					BUTTON_UNARY_MINUS_ID -> pushSymbolWrapper(field, "-")

					BUTTON_C_ID -> {
						stack.clear()
						ExUser32.INSTANCE.SetWindowText(field, "")
					}

					BUTTON_DIVIDE_ID -> pushOperation(field, "/")
					BUTTON_MULTIPLE_ID -> pushOperation(field, "*")
					BUTTON_MINUS_ID -> pushOperation(field, "-")
					BUTTON_PLUS_ID -> pushOperation(field, "+")

					BUTTON_FACTORIAL_ID -> pushOperation(field, "!")
					BUTTON_SQUARE_ID -> pushOperation(field, "s")
					BUTTON_INVERSE_ID -> pushOperation(field, "i")
					BUTTON_SQUARE_ROOT_ID -> pushOperation(field, "r")

					BUTTON_EQUALS_ID -> calculateWrapper(field)
				}
			}

			WM_GETMINMAXINFO -> {
				val info = ExUser32.MINMAXINFO(Pointer(lParam.toLong()))
				info.read()
				info.ptMinTrackSize!!.x = 260
				info.ptMinTrackSize!!.y = 453
				info.ptMaxTrackSize!!.x = 260
				info.ptMaxTrackSize!!.y = 453
				info.write()
			}

			WM_CLOSE -> ExUser32.INSTANCE.DestroyWindow(window)
			WM_DESTROY -> ExUser32.INSTANCE.PostQuitMessage(0)
		}
		return ExUser32.INSTANCE.DefWindowProc(window, msg, wParam, lParam)
	}

	private fun calculateWrapper(field: HWND?) {
		try {
			calculate(field)
		} catch (e: Exception) {
			stack.clear()
			ExUser32.INSTANCE.SetWindowText(field, "Error!")
		}
	}

	private fun calculate(field: HWND?) {
		val bufferSize = 1000
		val buffer = CharArray(bufferSize)
		ExUser32.INSTANCE.GetWindowText(field, buffer, bufferSize)

		if (stack.size == 2) {
			val operator = stack[1]

			if (operator in setOf("+", "-", "*", "/")) {
				stack.add(Native.toString(buffer))

				val operand1 = stack[0].toDouble()
				val operand2 = stack[2].toDouble()

				val result = when (operator) {
					"+" -> operand1 + operand2
					"-" -> operand1 - operand2
					"*" -> operand1 * operand2
					"/" -> operand1 / operand2
					else -> throw IllegalArgumentException("Invalid operator: $operator")
				}

				stack.clear()

				ExUser32.INSTANCE.SetWindowText(field, "$result")
			} else if (operator in setOf("!", "s", "i", "r")) {
				val operand = stack[0].toDouble()

				val result = when (operator) {
					"!" -> factorial[operand.toInt()].toDouble()
					"s" -> operand * operand
					"i" -> 1.0 / operand
					"r" -> sqrt(operand)
					else -> throw IllegalArgumentException("Invalid operator: $operator")
				}

				stack.clear()

				ExUser32.INSTANCE.SetWindowText(field, "$result")
			}
		}
	}

	private fun pushOperation(field: HWND?, operation: String) {
		val bufferSize = 1000
		val buffer = CharArray(bufferSize)
		ExUser32.INSTANCE.GetWindowText(field, buffer, bufferSize)

		if (stack.isEmpty()) {
			stack.add(Native.toString(buffer))
			stack.add(operation)
			ExUser32.INSTANCE.SetWindowText(field, "")
		} else {
			stack.clear()
			ExUser32.INSTANCE.SetWindowText(field, "Error!")
		}
	}

	private fun pushSymbolWrapper(field: HWND?, symbol: String) {
		val bufferSize = 1000
		val buffer = CharArray(bufferSize)
		ExUser32.INSTANCE.GetWindowText(field, buffer, bufferSize)
		val str = Native.toString(buffer)

		when (symbol) {
			"3.14", "2.72" -> {
				if (!str.contains(".")) {
					pushSymbol(field, symbol)
				}
			}

			"." -> {
				if (!str.contains(".") && str.isNotEmpty()) {
					pushSymbol(field, symbol)
				}
			}

			"-" -> {
				if (str.isEmpty()) {
					pushSymbol(field, symbol)
				}
			}

			else -> {
				if (stack.isEmpty()) {
					pushSymbol(field, symbol)
				} else {
					val operator = stack[1]

					if (operator !in setOf("!", "s", "i", "r")) {
						pushSymbol(field, symbol)
					}
				}
			}
		}
	}

	private fun pushSymbol(field: HWND?, number: String) {
		val bufferSize = 1000
		val buffer = CharArray(bufferSize)
		ExUser32.INSTANCE.GetWindowText(field, buffer, bufferSize)
		ExUser32.INSTANCE.SetWindowText(field, Native.toString(buffer).replace("Error!", "") + number)
	}

	private fun registerField(window: HWND): HWND {
		return ExUser32.INSTANCE.CreateWindowEx(
			0,
			"STATIC",
			"",
			WS_TABSTOP or WS_VISIBLE or WS_CHILD or BS_DEFPUSHBUTTON,
			0,
			0,
			238,
			50,
			window,
			null,
			null,
			null
		)
	}

	private fun registerButton(window: HWND, id: Int, text: String, gridX: Int, gridY: Int) {
		val buttonWidth = 60
		val buttonHeight = 60

		ExUser32.INSTANCE.CreateWindowEx(
			0,
			"BUTTON",
			text,
			WS_TABSTOP or WS_VISIBLE or WS_CHILD or BS_DEFPUSHBUTTON,
			buttonWidth * gridX,
			50 + buttonHeight * gridY,
			buttonWidth,
			buttonHeight,
			window,
			HMENU(Pointer(id.toLong())),
			null,
			null
		)
	}

	private fun WPARAM.loword(): Int = (this.toLong() and 0xFFFF).toInt()
}
package hummel

import com.sun.jna.Native
import com.sun.jna.Pointer
import com.sun.jna.platform.win32.WinDef.*
import com.sun.jna.platform.win32.WinUser.*
import kotlin.math.max
import kotlin.math.sqrt

private const val WM_COMMAND: Int = 0x0111
private const val COLOR_WINDOW: Int = 0x5

private const val BUTTON_0_ID: Int = 0
private const val BUTTON_1_ID: Int = 1
private const val BUTTON_2_ID: Int = 2
private const val BUTTON_3_ID: Int = 3
private const val BUTTON_4_ID: Int = 4
private const val BUTTON_5_ID: Int = 5
private const val BUTTON_6_ID: Int = 6
private const val BUTTON_7_ID: Int = 7
private const val BUTTON_8_ID: Int = 8
private const val BUTTON_9_ID: Int = 9
private const val BUTTON_C_ID: Int = 10
private const val BUTTON_DIVIDE_ID: Int = 11
private const val BUTTON_DOT_ID: Int = 12
private const val BUTTON_EQUALS_ID: Int = 13
private const val BUTTON_E_ID: Int = 14
private const val BUTTON_FACTORIAL_ID: Int = 15
private const val BUTTON_INVERSE_ID: Int = 16
private const val BUTTON_MINUS_ID: Int = 17
private const val BUTTON_MULTIPLE_ID: Int = 18
private const val BUTTON_PI_ID: Int = 19
private const val BUTTON_PLUS_ID: Int = 20
private const val BUTTON_SQUARE_ID: Int = 21
private const val BUTTON_SQUARE_ROOT_ID: Int = 22
private const val BUTTON_UNARY_MINUS_ID: Int = 23

private const val DEFAULT_CAPACITY: Int = 100

private lateinit var field: HWND
private lateinit var data: MutableList<String>

private val factorial: Array<Int> = arrayOf(1, 1, 2, 6, 24, 120, 720, 5040, 40320, 362880, 3628800, 39916800, 479001600)

fun main() {
	val className = "HummelCalculator"
	val windowTitle = "WinAPI"

	val windowClass = WNDCLASSEX()
	windowClass.cbSize = windowClass.size()
	windowClass.style = 0
	windowClass.lpfnWndProc = WndProc()
	windowClass.cbClsExtra = 0
	windowClass.cbWndExtra = 0
	windowClass.hInstance = null
	windowClass.hIcon = null
	windowClass.hCursor = null
	windowClass.hbrBackground = HBRUSH(Pointer(COLOR_WINDOW.toLong()))
	windowClass.lpszMenuName = null
	windowClass.lpszClassName = className
	windowClass.hIcon = null

	ExUser32.INSTANCE.RegisterClassEx(windowClass)

	val screenWidth = ExUser32.INSTANCE.GetSystemMetrics(SM_CXSCREEN)
	val screenHeight = ExUser32.INSTANCE.GetSystemMetrics(SM_CYSCREEN)

	val windowWidth = 260
	val windowHeight = 458

	val windowX = max(0.0, ((screenWidth - windowWidth) / 2).toDouble()).toInt()
	val windowY = max(0.0, ((screenHeight - windowHeight) / 2).toDouble()).toInt()

	ExUser32.INSTANCE.CreateWindowEx(
		0,
		className,
		windowTitle,
		WS_VISIBLE or WS_CAPTION or WS_SYSMENU,
		windowX,
		windowY,
		windowWidth,
		windowHeight,
		null,
		null,
		null,
		null
	)

	val msg = MSG()
	while (ExUser32.INSTANCE.GetMessage(msg, null, 0, 0) != 0) {
		ExUser32.INSTANCE.TranslateMessage(msg)
		ExUser32.INSTANCE.DispatchMessage(msg)
	}
}

private class WndProc : WindowProc {
	override fun callback(window: HWND, msg: Int, wParam: WPARAM, lParam: LPARAM): LRESULT {
		when (msg) {
			WM_CREATE -> {
				data = ArrayList()
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
					BUTTON_0_ID -> pushSymbolWrapper("0")
					BUTTON_1_ID -> pushSymbolWrapper("1")
					BUTTON_2_ID -> pushSymbolWrapper("2")
					BUTTON_3_ID -> pushSymbolWrapper("3")
					BUTTON_4_ID -> pushSymbolWrapper("4")
					BUTTON_5_ID -> pushSymbolWrapper("5")
					BUTTON_6_ID -> pushSymbolWrapper("6")
					BUTTON_7_ID -> pushSymbolWrapper("7")
					BUTTON_8_ID -> pushSymbolWrapper("8")
					BUTTON_9_ID -> pushSymbolWrapper("9")

					BUTTON_E_ID -> pushSymbolWrapper("2.72")
					BUTTON_PI_ID -> pushSymbolWrapper("3.14")

					BUTTON_DOT_ID -> pushSymbolWrapper(".")
					BUTTON_UNARY_MINUS_ID -> pushSymbolWrapper("-")

					BUTTON_C_ID -> {
						data.clear()
						ExUser32.INSTANCE.SetWindowText(field, "")
					}

					BUTTON_DIVIDE_ID -> pushOperation("/")
					BUTTON_MULTIPLE_ID -> pushOperation("*")
					BUTTON_MINUS_ID -> pushOperation("-")
					BUTTON_PLUS_ID -> pushOperation("+")

					BUTTON_FACTORIAL_ID -> pushOperation("!")
					BUTTON_SQUARE_ID -> pushOperation("s")
					BUTTON_INVERSE_ID -> pushOperation("i")
					BUTTON_SQUARE_ROOT_ID -> pushOperation("r")

					BUTTON_EQUALS_ID -> calculateWrapper()
				}
			}

			WM_CLOSE -> ExUser32.INSTANCE.DestroyWindow(window)
			WM_DESTROY -> ExUser32.INSTANCE.PostQuitMessage(0)
		}
		return ExUser32.INSTANCE.DefWindowProc(window, msg, wParam, lParam)
	}

	private fun calculateWrapper() {
		try {
			calculate(field)
		} catch (e: Exception) {
			data.clear()
			ExUser32.INSTANCE.SetWindowText(field, "Error!")
		}
	}

	private fun calculate(field: HWND?) {
		val buffer = CharArray(DEFAULT_CAPACITY)
		ExUser32.INSTANCE.GetWindowText(field, buffer, DEFAULT_CAPACITY)

		if (data.size == 2) {
			val operator = data[1]

			if (operator in setOf("+", "-", "*", "/")) {
				data.add(Native.toString(buffer))

				val operand1 = data[0].toDouble()
				val operand2 = data[2].toDouble()

				val result = when (operator) {
					"+" -> operand1 + operand2
					"-" -> operand1 - operand2
					"*" -> operand1 * operand2
					"/" -> operand1 / operand2
					else -> throw IllegalArgumentException("Invalid operator: $operator")
				}

				data.clear()

				ExUser32.INSTANCE.SetWindowText(field, "$result")
			} else if (operator in setOf("!", "s", "i", "r")) {
				val operand = data[0].toDouble()

				val result = when (operator) {
					"!" -> factorial[operand.toInt()].toDouble()
					"s" -> operand * operand
					"i" -> 1.0 / operand
					"r" -> sqrt(operand)
					else -> throw IllegalArgumentException("Invalid operator: $operator")
				}

				data.clear()

				ExUser32.INSTANCE.SetWindowText(field, "$result")
			}
		}
	}

	private fun pushOperation(operation: String) {
		val buffer = CharArray(DEFAULT_CAPACITY)
		ExUser32.INSTANCE.GetWindowText(field, buffer, DEFAULT_CAPACITY)

		if (data.isEmpty()) {
			data.add(Native.toString(buffer))
			data.add(operation)
			ExUser32.INSTANCE.SetWindowText(field, "")
		} else {
			data.clear()
			ExUser32.INSTANCE.SetWindowText(field, "Error!")
		}
	}

	private fun pushSymbolWrapper(symbol: String) {
		val buffer = CharArray(DEFAULT_CAPACITY)
		ExUser32.INSTANCE.GetWindowText(field, buffer, DEFAULT_CAPACITY)
		val str = Native.toString(buffer)

		when (symbol) {
			"3.14", "2.72", "-" -> {
				if (str.isEmpty()) {
					pushSymbol(symbol)
				}
			}

			"." -> {
				if (!str.contains(".") && str.isNotEmpty()) {
					pushSymbol(symbol)
				}
			}

			else -> {
				if (data.isEmpty()) {
					pushSymbol(symbol)
				} else {
					val operator = data[1]

					if (operator !in setOf("!", "s", "i", "r")) {
						pushSymbol(symbol)
					}
				}
			}
		}
	}

	private fun pushSymbol(symbol: String) {
		val buffer = CharArray(DEFAULT_CAPACITY)
		ExUser32.INSTANCE.GetWindowText(field, buffer, DEFAULT_CAPACITY)
		val str = Native.toString(buffer)
		if (str == "Error!") {
			ExUser32.INSTANCE.SetWindowText(field, symbol)
		} else {
			ExUser32.INSTANCE.SetWindowText(field, str + symbol)
		}
	}

	private fun registerField(window: HWND): HWND {
		return ExUser32.INSTANCE.CreateWindowEx(
			0,
			"STATIC",
			"",
			WS_TABSTOP or WS_VISIBLE or WS_CHILD,
			1,
			1,
			239,
			48,
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
			WS_TABSTOP or WS_VISIBLE or WS_CHILD,
			buttonWidth * gridX + 1,
			buttonHeight * gridY + 50,
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
package hummel

import com.sun.jna.Native
import com.sun.jna.Pointer
import com.sun.jna.platform.win32.WinDef.*
import com.sun.jna.platform.win32.WinUser.*
import hummel.ExUser32.MINMAXINFO
import kotlin.math.max
import kotlin.math.sqrt

const val WM_COMMAND: Int = 0x0111
const val WM_GETMINMAXINFO: Int = 0x0024

var i: Int = 0

var BUTTON_0_ID: Int = i++
var BUTTON_1_ID: Int = i++
var BUTTON_2_ID: Int = i++
var BUTTON_3_ID: Int = i++
var BUTTON_4_ID: Int = i++
var BUTTON_5_ID: Int = i++
var BUTTON_6_ID: Int = i++
var BUTTON_7_ID: Int = i++
var BUTTON_8_ID: Int = i++
var BUTTON_9_ID: Int = i++
var BUTTON_C_ID: Int = i++
var BUTTON_DIVIDE_ID: Int = i++
var BUTTON_DOT_ID: Int = i++
var BUTTON_EQUALS_ID: Int = i++
var BUTTON_E_ID: Int = i++
var BUTTON_FACTORIAL_ID: Int = i++
var BUTTON_INVERSE_ID: Int = i++
var BUTTON_MINUS_ID: Int = i++
var BUTTON_MULTIPLE_ID: Int = i++
var BUTTON_PI_ID: Int = i++
var BUTTON_PLUS_ID: Int = i++
var BUTTON_SQUARE_ID: Int = i++
var BUTTON_SQUARE_ROOT_ID: Int = i++
var BUTTON_UNARY_MINUS_ID: Int = i++

var stack: MutableList<String>? = null

fun main() {
	val className = "HummelCalculator"
	val windowTitle = "WinAPI"
	var field: HWND? = null

	val windowClass = WNDCLASSEX()
	windowClass.lpfnWndProc = WindowProc { window, msg, wParam, lParam ->
		when (msg) {
			WM_CREATE -> {
				field = registerField(window)
				stack = ArrayList()

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
				field?.let {
					val buttonId = wParam.loword().toInt()

					when (buttonId) {
						BUTTON_0_ID -> pushSymbolWrapper(it, "0")
						BUTTON_1_ID -> pushSymbolWrapper(it, "1")
						BUTTON_2_ID -> pushSymbolWrapper(it, "2")
						BUTTON_3_ID -> pushSymbolWrapper(it, "3")
						BUTTON_4_ID -> pushSymbolWrapper(it, "4")
						BUTTON_5_ID -> pushSymbolWrapper(it, "5")
						BUTTON_6_ID -> pushSymbolWrapper(it, "6")
						BUTTON_7_ID -> pushSymbolWrapper(it, "7")
						BUTTON_8_ID -> pushSymbolWrapper(it, "8")
						BUTTON_9_ID -> pushSymbolWrapper(it, "9")

						BUTTON_E_ID -> pushSymbolWrapper(it, "2.72")
						BUTTON_PI_ID -> pushSymbolWrapper(it, "3.14")

						BUTTON_DOT_ID -> pushSymbolWrapper(it, ".")
						BUTTON_UNARY_MINUS_ID -> pushSymbolWrapper(it, "-")

						BUTTON_C_ID -> {
							stack?.clear()
							ExUser32.INSTANCE.SetWindowText(it, "")
						}

						BUTTON_DIVIDE_ID -> pushOperation(it, "/")
						BUTTON_MULTIPLE_ID -> pushOperation(it, "*")
						BUTTON_MINUS_ID -> pushOperation(it, "-")
						BUTTON_PLUS_ID -> pushOperation(it, "+")

						BUTTON_FACTORIAL_ID -> pushOperation(it, "!")
						BUTTON_SQUARE_ID -> pushOperation(it, "s")
						BUTTON_INVERSE_ID -> pushOperation(it, "i")
						BUTTON_SQUARE_ROOT_ID -> pushOperation(it, "r")

						BUTTON_EQUALS_ID -> calculateWrapper(it)

						else -> {}
					}
				}
			}

			WM_GETMINMAXINFO -> {
				val info = MINMAXINFO(Pointer(lParam.toLong()))
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
		return@WindowProc ExUser32.INSTANCE.DefWindowProc(window, msg, wParam, lParam)
	}
	windowClass.lpszClassName = className

	ExUser32.INSTANCE.RegisterClassEx(windowClass)

	val screenWidth = ExUser32.INSTANCE.GetSystemMetrics(SM_CXSCREEN)
	val screenHeight = ExUser32.INSTANCE.GetSystemMetrics(SM_CYSCREEN)

	val windowWidth = 260
	val windowHeight = 453

	val windowX = max(0, (screenWidth - windowWidth) / 2)
	val windowY = max(0, (screenHeight - windowHeight) / 2)

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

fun calculateWrapper(field: HWND) {
	try {
		calculate(field)
	} catch (e: Exception) {
		stack?.clear()
		ExUser32.INSTANCE.SetWindowText(field, "Error!")
	}
}

fun calculate(field: HWND) {
	val bufferSize = 1000
	val buffer = CharArray(bufferSize)
	ExUser32.INSTANCE.GetWindowText(field, buffer, bufferSize)

	stack?.let {
		if (it.size == 2) {
			val operator = it[1]

			if (operator in setOf("+", "-", "*", "/")) {
				it.add(Native.toString(buffer))

				val operand1 = it[0].toDouble()
				val operand2 = it[2].toDouble()

				val result = when (operator) {
					"+" -> operand1 + operand2
					"-" -> operand1 - operand2
					"*" -> operand1 * operand2
					"/" -> operand1 / operand2
					else -> throw IllegalArgumentException("Invalid operator: $operator")
				}

				it.clear()

				ExUser32.INSTANCE.SetWindowText(field, "$result")
			} else if (operator in setOf("!", "s", "i", "r")) {
				val operand = it[0].toDouble()

				val result = when (operator) {
					"!" -> {
						if (operand.toInt().toDouble() == operand) {
							factorial[operand.toInt()]
						} else {
							throw IllegalArgumentException("Invalid operand: $operand")
						}
					}

					"s" -> operand * operand
					"i" -> 1.0 / operand
					"r" -> sqrt(operand)
					else -> throw IllegalArgumentException("Invalid operator: $operator")
				}

				it.clear()

				ExUser32.INSTANCE.SetWindowText(field, "$result")
			}
		}
	}
}

fun pushOperation(field: HWND, operation: String) {
	val bufferSize = 1000
	val buffer = CharArray(bufferSize)
	ExUser32.INSTANCE.GetWindowText(field, buffer, bufferSize)

	stack?.let {
		if (it.size == 0) {
			it.add(Native.toString(buffer))
			it.add(operation)
			ExUser32.INSTANCE.SetWindowText(field, "")
		} else {
			it.clear()
			ExUser32.INSTANCE.SetWindowText(field, "Error!")
		}
	}
}

fun pushSymbolWrapper(field: HWND, symbol: String) {
	val bufferSize = 1000
	val buffer = CharArray(bufferSize)
	ExUser32.INSTANCE.GetWindowText(field, buffer, bufferSize)
	val str = Native.toString(buffer)

	if (symbol == "3.14" || symbol == "2.72") {
		if (!str.contains(".")) {
			pushSymbol(field, symbol)
		}
	} else if (symbol == ".") {
		if (!str.contains(".") && str.isNotEmpty()) {
			pushSymbol(field, symbol)
		}
	} else if (symbol == "-") {
		if (str.isEmpty()) {
			pushSymbol(field, symbol)
		}
	} else {
		stack?.let {
			if (it.size != 0) {
				val operator = it[1]

				if (operator !in setOf("!", "s", "i", "r")) {
					pushSymbol(field, symbol)
				}
			} else {
				pushSymbol(field, symbol)
			}
		} ?: throw Exception()
	}
}


fun pushSymbol(field: HWND, number: String) {
	val bufferSize = 1000
	val buffer = CharArray(bufferSize)
	ExUser32.INSTANCE.GetWindowText(field, buffer, bufferSize)
	ExUser32.INSTANCE.SetWindowText(field, Native.toString(buffer).replace("Error!", "") + number)
}

private fun registerField(window: HWND?): HWND? {
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

private fun registerButton(window: HWND?, id: Int, text: String, gridX: Int, gridY: Int) {
	val buttonWidth = 60
	val buttonHeight = 60

	ExUser32.INSTANCE.CreateWindowEx(
		0,
		"BUTTON",
		text,
		WS_TABSTOP or WS_VISIBLE or WS_CHILD or BS_DEFPUSHBUTTON,
		0 + buttonWidth * gridX,
		50 + buttonHeight * gridY,
		buttonWidth,
		buttonHeight,
		window,
		HMENU(Pointer(id.toLong())),
		null,
		null
	)
}

private fun WPARAM.loword(): Long = this.toLong() and 0xFFFF

private val factorial = arrayOf(
	1, // 0!
	1, // 1!
	2, // 2!
	6, // 3!
	24, // 4!
	120, // 5!
	720, // 6!
	5040, // 7!
	40320, // 8!
	362880, // 9!
	3628800, // 10!
	39916800, // 11!
	479001600 // 12!
)
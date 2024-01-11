package hummel

import kotlinx.cinterop.*
import platform.windows.*
import kotlin.math.max
import kotlin.math.sqrt

fun main() {
	memScoped {
		val className = "HummelCalculator"
		val windowTitle = "WinAPI"

		val windowClass = alloc<WNDCLASS>()
		windowClass.lpfnWndProc = staticCFunction(::wndProc)
		windowClass.lpszClassName = className.wcstr.ptr
		windowClass.hbrBackground = COLOR_WINDOW.toLong().toCPointer()

		RegisterClassW(windowClass.ptr)

		val screenWidth = GetSystemMetrics(SM_CXSCREEN)
		val screenHeight = GetSystemMetrics(SM_CYSCREEN)

		val windowWidth = 260
		val windowHeight = 453

		val windowX = max(0, (screenWidth - windowWidth) / 2)
		val windowY = max(0, (screenHeight - windowHeight) / 2)

		val window = CreateWindowExW(
			WS_EX_CLIENTEDGE.toUInt(),
			className,
			windowTitle,
			WS_OVERLAPPEDWINDOW.toUInt(),
			windowX,
			windowY,
			windowWidth,
			windowHeight,
			null,
			null,
			null,
			null
		)

		ShowWindow(window, SW_SHOW)
		UpdateWindow(window)

		val msg = alloc<MSG>()
		while (GetMessageW(msg.ptr, null, 0u, 0u) != 0) {
			TranslateMessage(msg.ptr)
			DispatchMessageW(msg.ptr)
		}
	}
}

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

var field: HWND? = null
var stack: MutableList<String>? = null

private fun wndProc(window: HWND?, msg: UINT, wParam: WPARAM, lParam: LPARAM): LRESULT {
	when (msg.toInt()) {
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
						SetWindowTextW(it, "")
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
			val info = lParam.toCPointer<MINMAXINFO>()!!
			info.pointed.ptMinTrackSize.x = 260
			info.pointed.ptMinTrackSize.y = 453
			info.pointed.ptMaxTrackSize.x = 260
			info.pointed.ptMaxTrackSize.y = 453
			return 0
		}

		WM_CLOSE -> DestroyWindow(window)
		WM_DESTROY -> PostQuitMessage(0)
	}
	return DefWindowProcW(window, msg, wParam, lParam)
}

fun calculateWrapper(field: HWND) {
	try {
		calculate(field)
	} catch (e: Exception) {
		stack?.clear()
		SetWindowTextW(field, "Error!")
	}
}

fun calculate(field: HWND) {
	memScoped {
		val bufferSize = 1000
		val buffer = allocArray<WCHARVar>(bufferSize)
		GetWindowTextW(field, buffer.reinterpret(), bufferSize)

		stack?.let {
			if (it.size == 2) {
				val operator = it[1]

				if (operator in setOf("+", "-", "*", "/")) {
					it.add(buffer.toKString())

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

					SetWindowTextW(field, "$result")
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

					SetWindowTextW(field, "$result")
				}
			}
		}
	}
}

fun pushOperation(field: HWND, operation: String) {
	memScoped {
		val bufferSize = 1000
		val buffer = allocArray<WCHARVar>(bufferSize)
		GetWindowTextW(field, buffer.reinterpret(), bufferSize)

		stack?.let {
			if (it.size == 0) {
				it.add(buffer.toKString())
				it.add(operation)
				SetWindowTextW(field, "")
			} else {
				it.clear()
				SetWindowTextW(field, "Error!")
			}
		}
	}
}

fun pushSymbolWrapper(field: HWND, symbol: String) {
	memScoped {
		val bufferSize = 1000
		val buffer = allocArray<WCHARVar>(bufferSize)
		GetWindowTextW(field, buffer.reinterpret(), bufferSize)
		val str = buffer.toKString()

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
}


fun pushSymbol(field: HWND, number: String) {
	memScoped {
		val bufferSize = 1000
		val buffer = allocArray<WCHARVar>(bufferSize)
		GetWindowTextW(field, buffer.reinterpret(), bufferSize)
		SetWindowTextW(field, buffer.toKString().replace("Error!", "") + number)
	}
}

private fun registerField(window: HWND?): HWND? {
	return CreateWindowExW(
		WS_EX_CLIENTEDGE.toUInt(),
		"STATIC",
		"",
		(WS_TABSTOP or WS_VISIBLE or WS_CHILD or BS_DEFPUSHBUTTON).toUInt(),
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

	CreateWindowExW(
		WS_EX_CLIENTEDGE.toUInt(),
		"BUTTON",
		text,
		(WS_TABSTOP or WS_VISIBLE or WS_CHILD or BS_DEFPUSHBUTTON).toUInt(),
		0 + buttonWidth * gridX,
		50 + buttonHeight * gridY,
		buttonWidth,
		buttonHeight,
		window,
		id.toLong().toCPointer(),
		null,
		null
	)
}

private fun ULong.loword(): ULong = this and 0xFFFFu

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
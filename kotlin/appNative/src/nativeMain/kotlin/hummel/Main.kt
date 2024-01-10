package hummel

import kotlinx.cinterop.*
import platform.windows.*
import kotlin.math.max

private const val width: Int = 260
private const val height: Int = 453

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

		val windowWidth = width
		val windowHeight = height

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

private fun wndProc(window: HWND?, msg: UINT, wParam: WPARAM, lParam: LPARAM): LRESULT {
	when (msg.toInt()) {
		WM_CREATE -> {
			registerField(window)

			registerButton(window, BUTTON_PI_ID, "p", 0, 0)
			registerButton(window, BUTTON_E_ID, "e", 1, 0)
			registerButton(window, BUTTON_C_ID, "C", 2, 0)
			registerButton(window, BUTTON_FACTORIAL_ID, "!", 3, 0)
			registerButton(window, BUTTON_INVERSE_ID, "1/x", 0, 1)
			registerButton(window, BUTTON_SQUARE_ID, "x^2", 1, 1)
			registerButton(window, BUTTON_SQUARE_ROOT_ID, "sqrt(x)", 2, 1)
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
			val buttonId = wParam.loword().toInt()

			when (buttonId) {
				BUTTON_0_ID -> {}
				BUTTON_1_ID -> {}
				BUTTON_2_ID -> {}
				BUTTON_3_ID -> {}
				BUTTON_4_ID -> {}
				BUTTON_5_ID -> {}
				BUTTON_6_ID -> {}
				BUTTON_7_ID -> {}
				BUTTON_8_ID -> {}
				BUTTON_9_ID -> {}
				BUTTON_C_ID -> {}
				BUTTON_DIVIDE_ID -> {}
				BUTTON_DOT_ID -> {}
				BUTTON_EQUALS_ID -> {}
				BUTTON_E_ID -> {}
				BUTTON_FACTORIAL_ID -> {}
				BUTTON_INVERSE_ID -> {}
				BUTTON_MINUS_ID -> {}
				BUTTON_MULTIPLE_ID -> {}
				BUTTON_PI_ID -> {}
				BUTTON_PLUS_ID -> {}
				BUTTON_SQUARE_ID -> {}
				BUTTON_SQUARE_ROOT_ID -> {}
				BUTTON_UNARY_MINUS_ID -> {}
			}
		}

		WM_CLOSE -> DestroyWindow(window)
		WM_DESTROY -> PostQuitMessage(0)
	}
	return DefWindowProcW(window, msg, wParam, lParam)
}

private fun registerField(window: HWND?) {
	CreateWindowExW(
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
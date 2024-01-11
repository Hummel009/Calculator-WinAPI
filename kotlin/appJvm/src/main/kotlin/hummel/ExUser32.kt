package hummel

import com.sun.jna.Native
import com.sun.jna.Pointer
import com.sun.jna.Structure
import com.sun.jna.Structure.FieldOrder
import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinDef.HWND
import com.sun.jna.platform.win32.WinDef.POINT
import com.sun.jna.win32.W32APIOptions

@Suppress("FunctionName", "unused")
internal interface ExUser32 : User32 {
	companion object {
		val INSTANCE: ExUser32 = Native.load("user32", ExUser32::class.java, W32APIOptions.DEFAULT_OPTIONS)
	}

	@FieldOrder("ptReserved", "ptMaxSize", "ptMaxPosition", "ptMinTrackSize", "ptMaxTrackSize")
	class MINMAXINFO(pointer: Pointer) : Structure(pointer) {
		@JvmField
		var ptReserved: POINT? = null

		@JvmField
		var ptMaxSize: POINT? = null

		@JvmField
		var ptMaxPosition: POINT? = null

		@JvmField
		var ptMinTrackSize: POINT? = null

		@JvmField
		var ptMaxTrackSize: POINT? = null
	}

	fun SetWindowText(hWnd: HWND?, lpString: String): Boolean
}
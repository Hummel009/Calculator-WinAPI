package hummel;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.win32.W32APIOptions;

@SuppressWarnings({"FunctionName", "unused", "InterfaceNeverImplemented", "UnusedReturnValue"})
interface ExUser32 extends User32 {
	ExUser32 INSTANCE = Native.load("user32", ExUser32.class, W32APIOptions.DEFAULT_OPTIONS);

	@Structure.FieldOrder({"ptReserved", "ptMaxSize", "ptMaxPosition", "ptMinTrackSize", "ptMaxTrackSize"})
	class MINMAXINFO extends Structure {
		public POINT ptReserved;
		public POINT ptMaxSize;
		public POINT ptMaxPosition;
		public POINT ptMinTrackSize;
		public POINT ptMaxTrackSize;

		public MINMAXINFO(Pointer pointer) {
			super(pointer);
		}
	}

	boolean SetWindowText(HWND hWnd, String lpString);
}
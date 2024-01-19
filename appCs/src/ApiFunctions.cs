#pragma warning disable

using System.Text;
using System.Runtime.InteropServices;

namespace Hummel
{
	public class WinAPI
	{
		public delegate IntPtr WndProc(IntPtr hWnd, int msg, IntPtr wParam, IntPtr lParam);

		[DllImport("user32.dll")]
		public static extern int RegisterClass(ref WNDCLASS lpwcx);

		[DllImport("user32.dll")]
		public static extern int GetSystemMetrics(SystemMetric smIndex);

		[DllImport("user32.dll")]
		public static extern IntPtr CreateWindowEx(
		   int dwExStyle,
		   int lpClassName,
		   string? lpWindowName,
		   int dwStyle,
		   int x,
		   int y,
		   int nWidth,
		   int nHeight,
		   IntPtr hWndParent,
		   IntPtr hMenu,
		   IntPtr hInstance,
		   IntPtr lpParam
		);

		[DllImport("user32.dll")]
		public static extern IntPtr CreateWindowEx(
			int dwExStyle,
			string? lpClassName,
			string? lpWindowName,
			int dwStyle,
			int x,
			int y,
			int nWidth,
			int nHeight,
			IntPtr hWndParent,
			IntPtr hMenu,
			IntPtr hInstance,
			IntPtr lpParam
		);

		[DllImport("user32.dll")]
		public static extern int GetMessage(out MSG lpMsg, IntPtr hWnd, int wMsgFilterMin, int wMsgFilterMax);

		[DllImport("user32.dll")]
		public static extern bool TranslateMessage(ref MSG lpMsg);

		[DllImport("user32.dll")]
		public static extern IntPtr DispatchMessage(ref MSG lpmsg);

		[DllImport("user32.dll")]
		public static extern IntPtr DefWindowProc(IntPtr hWnd, int uMsg, IntPtr wParam, IntPtr lParam);

		[DllImport("user32.dll")]
		public static extern bool SetWindowText(IntPtr hwnd, string? lpString);

		[DllImport("user32.dll")]
		public static extern void GetWindowText(IntPtr hWnd, StringBuilder? lpString, int nMaxCount);

		[DllImport("user32.dll")]
		public static extern int GetWindowTextLength(IntPtr hWnd);

		[DllImport("user32.dll")]
		public static extern bool DestroyWindow(IntPtr hwnd);

		[DllImport("user32.dll")]
		public static extern void PostQuitMessage(int exitCode);
	}
}
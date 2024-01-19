#pragma warning disable

using System.Text;
using System.Runtime.InteropServices;

namespace Hummel
{
    [StructLayout(LayoutKind.Sequential)]
    public struct WNDCLASS
    {
        public int style;
        public WinAPI.WndProc lpfnWndProc;
        public int cbClsExtra;
        public int cbWndExtra;
        public IntPtr hInstance;
        public IntPtr hIcon;
        public IntPtr hCursor;
        public IntPtr hbrBackground;
        public string lpszMenuName;
        public string lpszClassName;
    }

    [StructLayout(LayoutKind.Sequential)]
    public struct MSG
    {
        public IntPtr hwnd;
        public int message;
        public IntPtr wParam;
        public IntPtr lParam;
        public int time;
        public POINT pt;
    }

    [StructLayout(LayoutKind.Sequential)]
    public struct POINT
    {
        public int x;
        public int y;
    }

    public enum SystemMetric : int
    {
        SM_CXSCREEN = 0,
        SM_CYSCREEN = 1
    }
}
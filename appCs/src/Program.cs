using System.Runtime.InteropServices;

namespace Hummel
{
    public class Program
    {
        public const uint WM_COMMAND = 0x0111;
        public const uint COLOR_WINDOW = 0x5;
        public const uint WS_VISIBLE = 0x10000000;
        public const uint WS_CAPTION = 0x00C00000;
        public const uint WS_SYSMENU = 0x00080000;

        public const int BUTTON_0_ID = 0;
        public const int BUTTON_1_ID = 1;
        public const int BUTTON_2_ID = 2;
        public const int BUTTON_3_ID = 3;
        public const int BUTTON_4_ID = 4;
        public const int BUTTON_5_ID = 5;
        public const int BUTTON_6_ID = 6;
        public const int BUTTON_7_ID = 7;
        public const int BUTTON_8_ID = 8;
        public const int BUTTON_9_ID = 9;
        public const int BUTTON_C_ID = 10;
        public const int BUTTON_DIVIDE_ID = 11;
        public const int BUTTON_DOT_ID = 12;
        public const int BUTTON_EQUALS_ID = 13;
        public const int BUTTON_E_ID = 14;
        public const int BUTTON_FACTORIAL_ID = 15;
        public const int BUTTON_INVERSE_ID = 16;
        public const int BUTTON_MINUS_ID = 17;
        public const int BUTTON_MULTIPLE_ID = 18;
        public const int BUTTON_PI_ID = 19;
        public const int BUTTON_PLUS_ID = 20;
        public const int BUTTON_SQUARE_ID = 21;
        public const int BUTTON_SQUARE_ROOT_ID = 22;
        public const int BUTTON_UNARY_MINUS_ID = 23;

        public const int DEFAULT_CAPACITY = 100;
        private static readonly IntPtr field;
        private static readonly List<string>? data;
        private static WinAPI.WndProc? delegWndProc;

        private static readonly int[] FACTORIAL = [1, 1, 2, 6, 24, 120, 720, 5040, 40320, 362880, 3628800, 39916800, 479001600];

        public static void Main(string[] args)
        {
            var className = "HummelCalculator";
            var windowTitle = "WinAPI";
            delegWndProc = ParentWndProc;

            var windowClass = new WinAPI.WNDCLASS
            {
                style = 0,
                lpfnWndProc = delegWndProc,
                cbClsExtra = 0,
                cbWndExtra = 0,
                hInstance = 0,
                hIcon = 0,
                hCursor = 0,
                hbrBackground = 0,
                lpszMenuName = "",
                lpszClassName = className
            };

            var atom = WinAPI.RegisterClass(ref windowClass);

            var screenWidth = WinAPI.GetSystemMetrics(WinAPI.SystemMetric.SM_CXSCREEN);
            var screenHeight = WinAPI.GetSystemMetrics(WinAPI.SystemMetric.SM_CYSCREEN);

            var windowWidth = 260;
            var windowHeight = 458;

            var windowX = Math.Max(0, (screenWidth - windowWidth) / 2);
            var windowY = Math.Max(0, (screenHeight - windowHeight) / 2);

            WinAPI.CreateWindowEx(0, atom, windowTitle, WS_VISIBLE | WS_CAPTION | WS_SYSMENU, windowX, windowY, windowWidth, windowHeight, 0, 0, 0, 0);

            var msg = new WinAPI.MSG();
            while (WinAPI.GetMessage(out msg, 0, 0, 0) != 0)
            {
                WinAPI.TranslateMessage(ref msg);
                WinAPI.DispatchMessage(ref msg);
            }
        }

        private static IntPtr ParentWndProc(IntPtr hWnd, uint msg, IntPtr wParam, IntPtr lParam)
        {
            return WinAPI.DefWindowProc(hWnd, msg, wParam, lParam);
        }

    }
}
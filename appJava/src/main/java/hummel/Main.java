package hummel;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef.HMENU;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.WPARAM;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.sun.jna.platform.win32.WinUser.*;

public class Main {
	public static final int WM_COMMAND = 0x0111;
	public static final int WM_GETMINMAXINFO = 0x0024;

	public static final int BUTTON_0_ID = 0;
	public static final int BUTTON_1_ID = 1;
	public static final int BUTTON_2_ID = 2;
	public static final int BUTTON_3_ID = 3;
	public static final int BUTTON_4_ID = 4;
	public static final int BUTTON_5_ID = 5;
	public static final int BUTTON_6_ID = 6;
	public static final int BUTTON_7_ID = 7;
	public static final int BUTTON_8_ID = 8;
	public static final int BUTTON_9_ID = 9;
	public static final int BUTTON_C_ID = 10;
	public static final int BUTTON_DIVIDE_ID = 11;
	public static final int BUTTON_DOT_ID = 12;
	public static final int BUTTON_EQUALS_ID = 13;
	public static final int BUTTON_E_ID = 14;
	public static final int BUTTON_FACTORIAL_ID = 15;
	public static final int BUTTON_INVERSE_ID = 16;
	public static final int BUTTON_MINUS_ID = 17;
	public static final int BUTTON_MULTIPLE_ID = 18;
	public static final int BUTTON_PI_ID = 19;
	public static final int BUTTON_PLUS_ID = 20;
	public static final int BUTTON_SQUARE_ID = 21;
	public static final int BUTTON_SQUARE_ROOT_ID = 22;
	public static final int BUTTON_UNARY_MINUS_ID = 23;

	public static List<String> stack;
	public static HWND field;

	private static final int[] FACTORIAL = {1, 1, 2, 6, 24, 120, 720, 5040, 40320, 362880, 3628800, 39916800, 479001600};

	public static void main(String[] args) {
		var windowClass = new WNDCLASSEX();
		windowClass.lpfnWndProc = new WndProc();
		var className = "HummelCalculator";
		windowClass.lpszClassName = className;

		ExUser32.INSTANCE.RegisterClassEx(windowClass);

		var screenWidth = ExUser32.INSTANCE.GetSystemMetrics(SM_CXSCREEN);
		var screenHeight = ExUser32.INSTANCE.GetSystemMetrics(SM_CYSCREEN);

		var windowWidth = 260;
		var windowHeight = 453;

		var windowX = Math.max(0, (screenWidth - windowWidth) / 2);
		var windowY = Math.max(0, (screenHeight - windowHeight) / 2);

		var windowTitle = "WinAPI";
		var window = ExUser32.INSTANCE.CreateWindowEx(0, className, windowTitle, WS_OVERLAPPEDWINDOW, windowX, windowY, windowWidth, windowHeight, null, null, null, null);

		ExUser32.INSTANCE.ShowWindow(window, SW_SHOW);
		ExUser32.INSTANCE.UpdateWindow(window);

		var msg = new MSG();
		while (ExUser32.INSTANCE.GetMessage(msg, null, 0, 0) != 0) {
			ExUser32.INSTANCE.TranslateMessage(msg);
			ExUser32.INSTANCE.DispatchMessage(msg);
		}
	}

	public static class WndProc implements WindowProc {
		@Override
		public LRESULT callback(HWND window, int msg, WPARAM wParam, LPARAM lParam) {
			switch (msg) {
				case WM_CREATE -> {
					stack = new ArrayList<>();

					field = registerField(window);

					registerButton(window, BUTTON_PI_ID, "π", 0, 0);
					registerButton(window, BUTTON_E_ID, "e", 1, 0);
					registerButton(window, BUTTON_C_ID, "C", 2, 0);
					registerButton(window, BUTTON_FACTORIAL_ID, "x!", 3, 0);
					registerButton(window, BUTTON_INVERSE_ID, "1/x", 0, 1);
					registerButton(window, BUTTON_SQUARE_ID, "x^2", 1, 1);
					registerButton(window, BUTTON_SQUARE_ROOT_ID, "√x", 2, 1);
					registerButton(window, BUTTON_DIVIDE_ID, "/", 3, 1);
					registerButton(window, BUTTON_7_ID, "7", 0, 2);
					registerButton(window, BUTTON_8_ID, "8", 1, 2);
					registerButton(window, BUTTON_9_ID, "9", 2, 2);
					registerButton(window, BUTTON_MULTIPLE_ID, "*", 3, 2);
					registerButton(window, BUTTON_4_ID, "4", 0, 3);
					registerButton(window, BUTTON_5_ID, "5", 1, 3);
					registerButton(window, BUTTON_6_ID, "6", 2, 3);
					registerButton(window, BUTTON_MINUS_ID, "-", 3, 3);
					registerButton(window, BUTTON_1_ID, "1", 0, 4);
					registerButton(window, BUTTON_2_ID, "2", 1, 4);
					registerButton(window, BUTTON_3_ID, "3", 2, 4);
					registerButton(window, BUTTON_PLUS_ID, "+", 3, 4);
					registerButton(window, BUTTON_UNARY_MINUS_ID, "-", 0, 5);
					registerButton(window, BUTTON_0_ID, "0", 1, 5);
					registerButton(window, BUTTON_DOT_ID, ".", 2, 5);
					registerButton(window, BUTTON_EQUALS_ID, "=", 3, 5);
				}

				case WM_COMMAND -> {
					var buttonId = loword(wParam);

					switch (buttonId) {
						case BUTTON_0_ID -> pushSymbolWrapper(field, "0");
						case BUTTON_1_ID -> pushSymbolWrapper(field, "1");
						case BUTTON_2_ID -> pushSymbolWrapper(field, "2");
						case BUTTON_3_ID -> pushSymbolWrapper(field, "3");
						case BUTTON_4_ID -> pushSymbolWrapper(field, "4");
						case BUTTON_5_ID -> pushSymbolWrapper(field, "5");
						case BUTTON_6_ID -> pushSymbolWrapper(field, "6");
						case BUTTON_7_ID -> pushSymbolWrapper(field, "7");
						case BUTTON_8_ID -> pushSymbolWrapper(field, "8");
						case BUTTON_9_ID -> pushSymbolWrapper(field, "9");

						case BUTTON_E_ID -> pushSymbolWrapper(field, "2.72");
						case BUTTON_PI_ID -> pushSymbolWrapper(field, "3.14");

						case BUTTON_DOT_ID -> pushSymbolWrapper(field, ".");
						case BUTTON_UNARY_MINUS_ID -> pushSymbolWrapper(field, "-");

						case BUTTON_C_ID -> {
							stack.clear();
							ExUser32.INSTANCE.SetWindowText(field, "");
						}

						case BUTTON_DIVIDE_ID -> pushOperation(field, "/");
						case BUTTON_MULTIPLE_ID -> pushOperation(field, "*");
						case BUTTON_MINUS_ID -> pushOperation(field, "-");
						case BUTTON_PLUS_ID -> pushOperation(field, "+");

						case BUTTON_FACTORIAL_ID -> pushOperation(field, "!");
						case BUTTON_SQUARE_ID -> pushOperation(field, "s");
						case BUTTON_INVERSE_ID -> pushOperation(field, "i");
						case BUTTON_SQUARE_ROOT_ID -> pushOperation(field, "r");

						case BUTTON_EQUALS_ID -> calculateWrapper(field);
						default -> {
						}
					}
				}

				case WM_GETMINMAXINFO -> {
					var info = new ExUser32.MINMAXINFO(new Pointer(lParam.longValue()));
					info.read();
					info.ptMinTrackSize.x = 260;
					info.ptMinTrackSize.y = 453;
					info.ptMaxTrackSize.x = 260;
					info.ptMaxTrackSize.y = 453;
					info.write();
				}

				case WM_CLOSE -> ExUser32.INSTANCE.DestroyWindow(window);
				case WM_DESTROY -> ExUser32.INSTANCE.PostQuitMessage(0);
				default -> {
				}
			}
			return ExUser32.INSTANCE.DefWindowProc(window, msg, wParam, lParam);
		}

		private static void calculateWrapper(HWND field) {
			try {
				calculate(field);
			} catch (Exception e) {
				stack.clear();
				ExUser32.INSTANCE.SetWindowText(field, "Error!");
			}
		}

		private static void calculate(HWND field) {
			var bufferSize = 1000;
			var buffer = new char[bufferSize];
			ExUser32.INSTANCE.GetWindowText(field, buffer, bufferSize);

			if (stack.size() == 2) {
				var operator = stack.get(1);

				if (Set.of("+", "-", "*", "/").contains(operator)) {
					stack.add(Native.toString(buffer));

					var operand1 = Double.parseDouble(stack.get(0));
					var operand2 = Double.parseDouble(stack.get(2));

					var result = switch (operator) {
						case "+" -> operand1 + operand2;
						case "-" -> operand1 - operand2;
						case "*" -> operand1 * operand2;
						case "/" -> operand1 / operand2;
						default -> throw new IllegalArgumentException("Invalid operator: " + operator);
					};

					stack.clear();

					ExUser32.INSTANCE.SetWindowText(field, String.valueOf(result));
				} else if (Set.of("!", "s", "i", "r").contains(operator)) {
					var operand = Double.parseDouble(stack.getFirst());

					var result = switch (operator) {
						case "!" -> FACTORIAL[(int) operand];
						case "s" -> operand * operand;
						case "i" -> 1.0 / operand;
						case "r" -> Math.sqrt(operand);
						default -> throw new IllegalArgumentException("Invalid operator: " + operator);
					};

					stack.clear();

					ExUser32.INSTANCE.SetWindowText(field, String.valueOf(result));
				}
			}
		}

		private static void pushOperation(HWND field, String operation) {
			var bufferSize = 1000;
			var buffer = new char[bufferSize];
			ExUser32.INSTANCE.GetWindowText(field, buffer, bufferSize);

			if (stack.isEmpty()) {
				stack.add(Native.toString(buffer));
				stack.add(operation);
				ExUser32.INSTANCE.SetWindowText(field, "");
			} else {
				stack.clear();
				ExUser32.INSTANCE.SetWindowText(field, "Error!");
			}
		}

		private static void pushSymbolWrapper(HWND field, String symbol) {
			var bufferSize = 1000;
			var buffer = new char[bufferSize];
			ExUser32.INSTANCE.GetWindowText(field, buffer, bufferSize);
			var str = Native.toString(buffer);

			switch (symbol) {
				case "3.14", "2.72" -> {
					if (!str.contains(".")) {
						pushSymbol(field, symbol);
					}
				}
				case "." -> {
					if (!str.contains(".") && !str.isEmpty()) {
						pushSymbol(field, symbol);
					}
				}
				case "-" -> {
					if (str.isEmpty()) {
						pushSymbol(field, symbol);
					}
				}
				default -> {
					if (stack.isEmpty()) {
						pushSymbol(field, symbol);
					} else {
						var operator = stack.get(1);

						if (!Set.of("!", "s", "i", "r").contains(operator)) {
							pushSymbol(field, symbol);
						}
					}
				}
			}
		}

		private static void pushSymbol(HWND field, String number) {
			var bufferSize = 1000;
			var buffer = new char[bufferSize];
			ExUser32.INSTANCE.GetWindowText(field, buffer, bufferSize);
			ExUser32.INSTANCE.SetWindowText(field, Native.toString(buffer).replace("Error!", "") + number);
		}

		private static HWND registerField(HWND window) {
			return ExUser32.INSTANCE.CreateWindowEx(0, "STATIC", "", WS_TABSTOP | WS_VISIBLE | WS_CHILD | BS_DEFPUSHBUTTON, 0, 0, 238, 50, window, null, null, null);
		}

		private static void registerButton(HWND window, int id, String text, int gridX, int gridY) {
			var buttonWidth = 60;
			var buttonHeight = 60;

			ExUser32.INSTANCE.CreateWindowEx(0, "BUTTON", text, WS_TABSTOP | WS_VISIBLE | WS_CHILD | BS_DEFPUSHBUTTON, buttonWidth * gridX, 50 + buttonHeight * gridY, buttonWidth, buttonHeight, window, new HMENU(new Pointer(id)), null, null);
		}

		private static int loword(WPARAM wparam) {
			return (int) (wparam.longValue() & 0xFFFF);
		}
	}
}
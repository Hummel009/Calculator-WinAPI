#include <iostream>
#include <windows.h>
#include <vector>
#include <set>
#include <cmath>

using namespace std;

const int BUTTON_0_ID = 0;
const int BUTTON_1_ID = 1;
const int BUTTON_2_ID = 2;
const int BUTTON_3_ID = 3;
const int BUTTON_4_ID = 4;
const int BUTTON_5_ID = 5;
const int BUTTON_6_ID = 6;
const int BUTTON_7_ID = 7;
const int BUTTON_8_ID = 8;
const int BUTTON_9_ID = 9;
const int BUTTON_C_ID = 10;
const int BUTTON_DIVIDE_ID = 11;
const int BUTTON_DOT_ID = 12;
const int BUTTON_EQUALS_ID = 13;
const int BUTTON_E_ID = 14;
const int BUTTON_FACTORIAL_ID = 15;
const int BUTTON_INVERSE_ID = 16;
const int BUTTON_MINUS_ID = 17;
const int BUTTON_MULTIPLE_ID = 18;
const int BUTTON_PI_ID = 19;
const int BUTTON_PLUS_ID = 20;
const int BUTTON_SQUARE_ID = 21;
const int BUTTON_SQUARE_ROOT_ID = 22;
const int BUTTON_UNARY_MINUS_ID = 23;

static HWND field;
static vector<string> stack;

static vector<int> factorial = {1, 1, 2, 6, 24, 120, 720, 5040, 40320, 362880, 3628800, 39916800, 479001600};

static void registerButton(HWND window, int id, string text, int gridX, int gridY);
static HWND registerField(HWND window);
static void pushSymbolWrapper(HWND field, string symbol);
static void pushOperation(HWND field, string operation);
static void calculateWrapper(HWND field);

static LRESULT wndProc(HWND window, UINT msg, WPARAM wParam, LPARAM lParam)
{
	int buttonId = LOWORD(wParam);
	MINMAXINFO *info = (MINMAXINFO *)lParam;

	switch (msg)
	{
	case WM_CREATE:
		field = registerField(window);

		registerButton(window, BUTTON_PI_ID, "p", 0, 0);
		registerButton(window, BUTTON_E_ID, "e", 1, 0);
		registerButton(window, BUTTON_C_ID, "C", 2, 0);
		registerButton(window, BUTTON_FACTORIAL_ID, "x!", 3, 0);
		registerButton(window, BUTTON_INVERSE_ID, "1/x", 0, 1);
		registerButton(window, BUTTON_SQUARE_ID, "x^2", 1, 1);
		registerButton(window, BUTTON_SQUARE_ROOT_ID, "sqrt(x)", 2, 1);
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
		break;
	case WM_COMMAND:
		switch (buttonId)
		{
		case BUTTON_0_ID:
			pushSymbolWrapper(field, "0");
			break;
		case BUTTON_1_ID:
			pushSymbolWrapper(field, "1");
			break;
		case BUTTON_2_ID:
			pushSymbolWrapper(field, "2");
			break;
		case BUTTON_3_ID:
			pushSymbolWrapper(field, "3");
			break;
		case BUTTON_4_ID:
			pushSymbolWrapper(field, "4");
			break;
		case BUTTON_5_ID:
			pushSymbolWrapper(field, "5");
			break;
		case BUTTON_6_ID:
			pushSymbolWrapper(field, "6");
			break;
		case BUTTON_7_ID:
			pushSymbolWrapper(field, "7");
			break;
		case BUTTON_8_ID:
			pushSymbolWrapper(field, "8");
			break;
		case BUTTON_9_ID:
			pushSymbolWrapper(field, "9");
			break;
		case BUTTON_E_ID:
			pushSymbolWrapper(field, "2.72");
			break;
		case BUTTON_PI_ID:
			pushSymbolWrapper(field, "3.14");
			break;
		case BUTTON_DOT_ID:
			pushSymbolWrapper(field, ".");
			break;
		case BUTTON_UNARY_MINUS_ID:
			pushSymbolWrapper(field, "-");
			break;
		case BUTTON_C_ID:
			stack.clear();
			SetWindowText(field, "");
			break;
		case BUTTON_DIVIDE_ID:
			pushOperation(field, "/");
			break;
		case BUTTON_MULTIPLE_ID:
			pushOperation(field, "*");
			break;
		case BUTTON_MINUS_ID:
			pushOperation(field, "-");
			break;
		case BUTTON_PLUS_ID:
			pushOperation(field, "+");
			break;
		case BUTTON_FACTORIAL_ID:
			pushOperation(field, "!");
			break;
		case BUTTON_SQUARE_ID:
			pushOperation(field, "s");
			break;
		case BUTTON_INVERSE_ID:
			pushOperation(field, "i");
			break;
		case BUTTON_SQUARE_ROOT_ID:
			pushOperation(field, "r");
			break;
		case BUTTON_EQUALS_ID:
			calculateWrapper(field);
			break;
		}
		break;

	case WM_GETMINMAXINFO:
		info->ptMinTrackSize.x = 260;
		info->ptMinTrackSize.y = 453;
		info->ptMaxTrackSize.x = 260;
		info->ptMaxTrackSize.y = 453;
		break;

	case WM_CLOSE:
		DestroyWindow(window);
		break;
	case WM_DESTROY:
		PostQuitMessage(0);
		break;
	}
	return DefWindowProcA(window, msg, wParam, lParam);
}

int main()
{
	string className = "HummelCalculator";

	WNDCLASSEX wc;
	wc.cbSize = sizeof(WNDCLASSEX);
	wc.style = CS_HREDRAW | CS_VREDRAW;
	wc.lpfnWndProc = wndProc;
	wc.cbClsExtra = 0;
	wc.cbWndExtra = 0;
	wc.hInstance = NULL;
	wc.hIcon = LoadIcon(NULL, IDI_APPLICATION);
	wc.hCursor = LoadCursor(NULL, IDC_ARROW);
	wc.hbrBackground = (HBRUSH)(COLOR_WINDOW + 1);
	wc.lpszMenuName = NULL;
	wc.lpszClassName = className.c_str();
	wc.hIconSm = LoadIcon(NULL, IDI_APPLICATION);

	RegisterClassExA(&wc);

	int screenWidth = GetSystemMetrics(SM_CXSCREEN);
	int screenHeight = GetSystemMetrics(SM_CYSCREEN);

	int windowWidth = 260;
	int windowHeight = 453;

	int windowX = max(0, (screenWidth - windowWidth) / 2);
	int windowY = max(0, (screenHeight - windowHeight) / 2);

	string windowTitle = "WinAPI";
	HWND window = CreateWindowExA(
		WS_EX_CLIENTEDGE,
		className.c_str(),
		windowTitle.c_str(),
		WS_OVERLAPPEDWINDOW,
		windowX,
		windowY,
		windowWidth,
		windowHeight,
		NULL,
		NULL,
		NULL,
		NULL);

	ShowWindow(window, SW_SHOW);
	UpdateWindow(window);

	MSG msg;
	while (GetMessageA(&msg, NULL, 0u, 0u) != 0)
	{
		TranslateMessage(&msg);
		DispatchMessageA(&msg);
	}
}

static void calculate(HWND field);
static void calculateWrapper(HWND field)
{
	try
	{
		calculate(field);
	}
	catch (exception &e)
	{
		stack.clear();
		SetWindowText(field, "Error!");
	}
}

static void calculate(HWND field)
{
	int bufferSize = 1000;
	char *buffer = new char[1000];
	GetWindowText(field, buffer, bufferSize);

	if (stack.size() == 2)
	{
		string op = stack[1];
		set<string> ops1 = {"+", "-", "*", "/"};
		set<string> ops2 = {"!", "s", "i", "r"};

		if (ops1.count(op) > 0)
		{
			string str(buffer);
			stack.push_back(str);

			double operand1 = stod(stack[0]);
			double operand2 = stod(stack[2]);

			double result;
			if (op == "+")
			{
				result = operand1 + operand2;
			}
			else if (op == "-")
			{
				result = operand1 - operand2;
			}
			else if (op == "*")
			{
				result = operand1 * operand2;
			}
			else if (op == "/")
			{
				result = operand1 / operand2;
			}
			else
			{
				throw new exception();
			}

			stack.clear();

			SetWindowText(field, to_string(result).c_str());
		}
		else if (ops2.count(op) > 0)
		{
			double operand = stod(stack[0]);

			double result;
			if (op == "!")
			{
				result = factorial[(int)operand];
			}
			else if (op == "s")
			{
				result = operand * operand;
			}
			else if (op == "i")
			{
				result = 1.0 / operand;
			}
			else if (op == "r")
			{
				result = sqrt(operand);
			}
			else
			{
				throw new exception();
			}

			stack.clear();

			SetWindowText(field, to_string(result).c_str());
		}
	}
}

static void pushOperation(HWND field, string operation)
{
	int bufferSize = 1000;
	char *buffer = new char[1000];
	GetWindowText(field, buffer, bufferSize);

	if (stack.size() == 0)
	{
		string str(buffer);
		stack.push_back(str);
		stack.push_back(operation);
		SetWindowText(field, "");
	}
	else
	{
		stack.clear();
		SetWindowText(field, "Error!");
	}
}

static void pushSymbol(HWND field, string number);
static void pushSymbolWrapper(HWND field, string symbol)
{
	int bufferSize = 1000;
	char *buffer = new char[1000];
	GetWindowText(field, buffer, bufferSize);
	string str(buffer);

	if (symbol == "3.14" || symbol == "2.72")
	{
		if (str.find(".") == string::npos)
		{
			pushSymbol(field, symbol);
		}
	}
	else if (symbol == ".")
	{
		if (str.find(".") == string::npos && str.size() != 0)
		{
			pushSymbol(field, symbol);
		}
	}
	else if (symbol == "-")
	{
		if (str.size() == 0)
		{
			pushSymbol(field, symbol);
		}
	}
	else
	{
		if (stack.size() == 0)
		{
			pushSymbol(field, symbol);
		}
		else
		{
			string op = stack[1];
			set<string> ops = {"!", "s", "i", "r"};

			if (ops.count(op) <= 0)
			{
				pushSymbol(field, symbol);
			}
		}
	}
}

static void pushSymbol(HWND field, string number)
{
	int bufferSize = 1000;
	char *buffer = new char[1000];
	GetWindowText(field, buffer, bufferSize);
	string str(buffer);
	size_t found = str.find("Error!");
	if (found != string::npos)
	{
		str.replace(found, 6, "");
	}
	SetWindowText(field, (str + number).c_str());
}

static HWND registerField(HWND window)
{
	return CreateWindowEx(
		0,
		"STATIC",
		"",
		WS_TABSTOP | WS_VISIBLE | WS_CHILD | BS_DEFPUSHBUTTON,
		0,
		0,
		238,
		50,
		window,
		NULL,
		NULL,
		NULL);
}

static void registerButton(HWND window, int id, string text, int gridX, int gridY)
{
	int buttonWidth = 60;
	int buttonHeight = 60;

	CreateWindowEx(
		0,
		"BUTTON",
		text.c_str(),
		WS_TABSTOP | WS_VISIBLE | WS_CHILD | BS_DEFPUSHBUTTON,
		buttonWidth * gridX,
		50 + buttonHeight * gridY,
		buttonWidth,
		buttonHeight,
		window,
		reinterpret_cast<HMENU>(id),
		NULL,
		NULL);
}
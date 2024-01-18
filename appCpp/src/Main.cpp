#include <iostream>
#include <windows.h>
#include <vector>
#include <set>
#include <cmath>

using namespace std;

#define BUTTON_0_ID 0
#define BUTTON_1_ID 1
#define BUTTON_2_ID 2
#define BUTTON_3_ID 3
#define BUTTON_4_ID 4
#define BUTTON_5_ID 5
#define BUTTON_6_ID 6
#define BUTTON_7_ID 7
#define BUTTON_8_ID 8
#define BUTTON_9_ID 9
#define BUTTON_C_ID 10
#define BUTTON_DIVIDE_ID 11
#define BUTTON_DOT_ID 12
#define BUTTON_EQUALS_ID 13
#define BUTTON_E_ID 14
#define BUTTON_FACTORIAL_ID 15
#define BUTTON_INVERSE_ID 16
#define BUTTON_MINUS_ID 17
#define BUTTON_MULTIPLE_ID 18
#define BUTTON_PI_ID 19
#define BUTTON_PLUS_ID 20
#define BUTTON_SQUARE_ID 21
#define BUTTON_SQUARE_ROOT_ID 22
#define BUTTON_UNARY_MINUS_ID 23

#define DEFAULT_CAPACITY 100

static HWND field;
static vector<string> data;

static vector<int> factorial = {1, 1, 2, 6, 24, 120, 720, 5040, 40320, 362880, 3628800, 39916800, 479001600};

static void registerButton(HWND window, int id, string text, int gridX, int gridY);
static HWND registerField(HWND window);
static void pushSymbolWrapper(string symbol);
static void pushOperation(string operation);
static void calculateWrapper();

static LRESULT wndProc(HWND window, UINT msg, WPARAM wParam, LPARAM lParam)
{
	int buttonId = LOWORD(wParam);

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
			pushSymbolWrapper("0");
			break;
		case BUTTON_1_ID:
			pushSymbolWrapper("1");
			break;
		case BUTTON_2_ID:
			pushSymbolWrapper("2");
			break;
		case BUTTON_3_ID:
			pushSymbolWrapper("3");
			break;
		case BUTTON_4_ID:
			pushSymbolWrapper("4");
			break;
		case BUTTON_5_ID:
			pushSymbolWrapper("5");
			break;
		case BUTTON_6_ID:
			pushSymbolWrapper("6");
			break;
		case BUTTON_7_ID:
			pushSymbolWrapper("7");
			break;
		case BUTTON_8_ID:
			pushSymbolWrapper("8");
			break;
		case BUTTON_9_ID:
			pushSymbolWrapper("9");
			break;
		case BUTTON_E_ID:
			pushSymbolWrapper("2.72");
			break;
		case BUTTON_PI_ID:
			pushSymbolWrapper("3.14");
			break;
		case BUTTON_DOT_ID:
			pushSymbolWrapper(".");
			break;
		case BUTTON_UNARY_MINUS_ID:
			pushSymbolWrapper("-");
			break;
		case BUTTON_C_ID:
			data.clear();
			SetWindowTextA(field, "");
			break;
		case BUTTON_DIVIDE_ID:
			pushOperation("/");
			break;
		case BUTTON_MULTIPLE_ID:
			pushOperation("*");
			break;
		case BUTTON_MINUS_ID:
			pushOperation("-");
			break;
		case BUTTON_PLUS_ID:
			pushOperation("+");
			break;
		case BUTTON_FACTORIAL_ID:
			pushOperation("!");
			break;
		case BUTTON_SQUARE_ID:
			pushOperation("s");
			break;
		case BUTTON_INVERSE_ID:
			pushOperation("i");
			break;
		case BUTTON_SQUARE_ROOT_ID:
			pushOperation("r");
			break;
		case BUTTON_EQUALS_ID:
			calculateWrapper();
			break;
		}
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
	string windowTitle = "WinAPI";

	WNDCLASSA windowClass;
	windowClass.style = 0;
	windowClass.lpfnWndProc = wndProc;
	windowClass.cbClsExtra = 0;
	windowClass.cbWndExtra = 0;
	windowClass.hInstance = NULL;
	windowClass.hIcon = NULL;
	windowClass.hCursor = NULL;
	windowClass.hbrBackground = (HBRUSH)(COLOR_WINDOW + 1);
	windowClass.lpszMenuName = NULL;
	windowClass.lpszClassName = className.c_str();

	RegisterClassA(&windowClass);

	int screenWidth = GetSystemMetrics(SM_CXSCREEN);
	int screenHeight = GetSystemMetrics(SM_CYSCREEN);

	int windowWidth = 248;
	int windowHeight = 440;

	int windowX = max(0, (screenWidth - windowWidth) / 2);
	int windowY = max(0, (screenHeight - windowHeight) / 2);

	CreateWindowExA(0, className.c_str(), windowTitle.c_str(), WS_VISIBLE | WS_CAPTION | WS_SYSMENU, windowX, windowY, windowWidth, windowHeight, NULL, NULL, NULL, NULL);

	MSG msg;
	while (GetMessageA(&msg, NULL, 0u, 0u) != 0)
	{
		TranslateMessage(&msg);
		DispatchMessageA(&msg);
	}
}

static void calculate();
static void calculateWrapper()
{
	try
	{
		calculate();
	}
	catch (exception &e)
	{
		data.clear();
		SetWindowTextA(field, "Error!");
	}
}

static void calculate()
{
	char *buffer = new char[DEFAULT_CAPACITY];
	GetWindowTextA(field, buffer, DEFAULT_CAPACITY);

	if (data.size() == 2)
	{
		string op = data[1];
		set<string> ops1 = {"+", "-", "*", "/"};
		set<string> ops2 = {"!", "s", "i", "r"};

		if (ops1.count(op) > 0)
		{
			string str(buffer);
			delete[] buffer;
			data.push_back(str);

			double operand1 = stod(data[0]);
			double operand2 = stod(data[2]);

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

			data.clear();

			SetWindowTextA(field, to_string(result).c_str());
		}
		else if (ops2.count(op) > 0)
		{
			double operand = stod(data[0]);

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

			data.clear();

			SetWindowTextA(field, to_string(result).c_str());
		}
	}
}

static void pushOperation(string operation)
{
	char *buffer = new char[DEFAULT_CAPACITY];
	GetWindowTextA(field, buffer, DEFAULT_CAPACITY);

	if (data.size() == 0)
	{
		string str(buffer);
		delete[] buffer;
		data.push_back(str);
		data.push_back(operation);
		SetWindowTextA(field, "");
	}
	else
	{
		data.clear();
		SetWindowTextA(field, "Error!");
	}
}

static void pushSymbol(string symbol);
static void pushSymbolWrapper(string symbol)
{
	char *buffer = new char[DEFAULT_CAPACITY];
	GetWindowTextA(field, buffer, DEFAULT_CAPACITY);
	string str(buffer);

	if (symbol == "3.14" || symbol == "2.72" || symbol == "-")
	{
		if (str.size() == 0)
		{
			pushSymbol(symbol);
		}
	}
	else if (symbol == ".")
	{
		if (str.find(".") == string::npos && str.size() != 0)
		{
			pushSymbol(symbol);
		}
	}
	else
	{
		if (data.size() == 0)
		{
			pushSymbol(symbol);
		}
		else
		{
			string op = data[1];
			set<string> ops = {"!", "s", "i", "r"};

			if (ops.count(op) <= 0)
			{
				pushSymbol(symbol);
			}
		}
	}
	delete[] buffer;
}

static void pushSymbol(string symbol)
{
	char *buffer = new char[DEFAULT_CAPACITY];
	GetWindowTextA(field, buffer, DEFAULT_CAPACITY);
	string str(buffer);
	if (str == "Error!")
	{
		SetWindowTextA(field, symbol.c_str());
	}
	else
	{
		SetWindowTextA(field, (str + symbol).c_str());
	}
	delete[] buffer;
}

static HWND registerField(HWND window)
{
	return CreateWindowExA(
		WS_EX_CLIENTEDGE,
		"STATIC",
		"",
		WS_TABSTOP | WS_VISIBLE | WS_CHILD,
		1,
		1,
		239,
		48,
		window,
		NULL,
		NULL,
		NULL);
}

static void registerButton(HWND window, int id, string text, int gridX, int gridY)
{
	int buttonWidth = 60;
	int buttonHeight = 60;

	CreateWindowExA(
		WS_EX_CLIENTEDGE,
		"BUTTON",
		text.c_str(),
		WS_TABSTOP | WS_VISIBLE | WS_CHILD,
		buttonWidth * gridX + 1,
		buttonHeight * gridY + 50,
		buttonWidth,
		buttonHeight,
		window,
		reinterpret_cast<HMENU>(id),
		NULL,
		NULL);
}
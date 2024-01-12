#include <windows.h>
#include <math.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

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
static char *data[3];
static BOOL data_presence[3];

static int factorial[] = {1, 1, 2, 6, 24, 120, 720, 5040, 40320, 362880, 3628800, 39916800, 479001600};

static void registerButton(HWND window, int id, char *text, int gridX, int gridY);
static HWND registerField(HWND window);
static void pushSymbolWrapper(char *symbol);
static void pushOperation(char *operation);
static void calculateWrapper();
static void initData();
static void resetData();
static void push(char *str);

static LRESULT wndProc(HWND window, UINT msg, WPARAM wParam, LPARAM lParam)
{
	int buttonId = LOWORD(wParam);
	MINMAXINFO *info = (MINMAXINFO *)lParam;

	switch (msg)
	{
	case WM_CREATE:
		initData();
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
			resetData();
			SetWindowText(field, "");
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
	char *className = "HummelCalculator";

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
	wc.lpszClassName = className;
	wc.hIconSm = LoadIcon(NULL, IDI_APPLICATION);

	RegisterClassExA(&wc);

	int screenWidth = GetSystemMetrics(SM_CXSCREEN);
	int screenHeight = GetSystemMetrics(SM_CYSCREEN);

	int windowWidth = 260;
	int windowHeight = 453;

	int windowX = max(0, (screenWidth - windowWidth) / 2);
	int windowY = max(0, (screenHeight - windowHeight) / 2);

	char *windowTitle = "WinAPI";
	HWND window = CreateWindowExA(
		WS_EX_CLIENTEDGE,
		className,
		windowTitle,
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

static void calculateWrapper()
{
	char *buffer = (char *)malloc(DEFAULT_CAPACITY);
	GetWindowText(field, buffer, DEFAULT_CAPACITY);

	if (data_presence[0] == TRUE && data_presence[1] == TRUE)
	{
		char *op = (char *)malloc(DEFAULT_CAPACITY);
		strcpy(op, data[1]);

		if (strcmp(op, "+") == 0 || strcmp(op, "-") == 0 || strcmp(op, "*") == 0 || strcmp(op, "/") == 0)
		{
			push(buffer);
			free(buffer);

			double operand1 = strtod(data[0], NULL);
			double operand2 = strtod(data[2], NULL);

			double result;
			if (strcmp(op, "+") == 0)
			{
				result = operand1 + operand2;
			}
			else if (strcmp(op, "-") == 0)
			{
				result = operand1 - operand2;
			}
			else if (strcmp(op, "*") == 0)
			{
				result = operand1 * operand2;
			}
			else if (strcmp(op, "/") == 0)
			{
				result = operand1 / operand2;
			}
			else
			{
				goto exception;
			}

			char *str = (char *)malloc(DEFAULT_CAPACITY);
			sprintf(str, "%f", result);
			SetWindowText(field, str);
			free(str);
		}
		else if (strcmp(op, "!") == 0 || strcmp(op, "s") == 0 || strcmp(op, "i") == 0 || strcmp(op, "r") == 0)
		{
			double operand = strtod(data[0], NULL);

			double result;
			if (strcmp(op, "!") == 0)
			{
				result = factorial[(int)operand];
			}
			else if (strcmp(op, "s") == 0)
			{
				result = operand * operand;
			}
			else if (strcmp(op, "i") == 0)
			{
				result = 1.0 / operand;
			}
			else if (strcmp(op, "r") == 0)
			{
				result = sqrt(operand);
			}
			else
			{
				goto exception;
			}

			char *str = (char *)malloc(DEFAULT_CAPACITY);
			sprintf(str, "%f", result);
			SetWindowText(field, str);
			free(str);
		}
	}
	resetData();
	return;

exception:
	resetData();
	SetWindowText(field, "Error!");
}

static void pushOperation(char *operation)
{
	char *buffer = (char *)malloc(DEFAULT_CAPACITY);
	GetWindowText(field, buffer, DEFAULT_CAPACITY);
	if (data_presence[0] == FALSE)
	{
		push(buffer);
		free(buffer);
		push(operation);
		SetWindowText(field, "");
	}
	else
	{
		resetData();
		SetWindowText(field, "Error!");
	}
}

static void pushSymbol(char *number);
static void pushSymbolWrapper(char *symbol)
{
	char *buffer = (char *)malloc(DEFAULT_CAPACITY);
	GetWindowText(field, buffer, DEFAULT_CAPACITY);
	if (strcmp(symbol, "3.14") == 0 || strcmp(symbol, "2.72") == 0)
	{
		if (strchr(buffer, '.') == NULL)
		{
			pushSymbol(symbol);
		}
	}
	else if (strcmp(symbol, ".") == 0)
	{
		if (strchr(buffer, '.') == NULL && strlen(buffer) != 0)
		{
			pushSymbol(symbol);
		}
	}
	else if (strcmp(symbol, "-") == 0)
	{
		if (strlen(buffer) == 0)
		{
			pushSymbol(symbol);
		}
	}
	else
	{
		if (strlen(buffer) == 0)
		{
			pushSymbol(symbol);
		}
		else
		{
			char *op = data[1];

			if (strcmp(op, "!") != 0 && strcmp(op, "s") != 0 && strcmp(op, "i") != 0 && strcmp(op, "r") != 0)
			{
				pushSymbol(symbol);
			}
		}
	}
	free(buffer);
}

static void pushSymbol(char *symbol)
{
	char *buffer = (char *)malloc(DEFAULT_CAPACITY);
	GetWindowText(field, buffer, DEFAULT_CAPACITY);
	if (strcmp(buffer, "Error!") == 0)
	{
		SetWindowText(field, symbol);
	}
	else
	{
		strcat(buffer, symbol);
		SetWindowText(field, buffer);
	}
	free(buffer);
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

static void registerButton(HWND window, int id, char *text, int gridX, int gridY)
{
	int buttonWidth = 60;
	int buttonHeight = 60;

	CreateWindowEx(
		0,
		"BUTTON",
		text,
		WS_TABSTOP | WS_VISIBLE | WS_CHILD | BS_DEFPUSHBUTTON,
		buttonWidth * gridX,
		50 + buttonHeight * gridY,
		buttonWidth,
		buttonHeight,
		window,
		(HMENU)id,
		NULL,
		NULL);
}

static void push(char *str)
{
	if (data_presence[0] == FALSE)
	{
		strcpy(data[0], str);
		data_presence[0] = TRUE;
		return;
	}
	else if (data_presence[1] == FALSE)
	{
		strcpy(data[1], str);
		data_presence[1] = TRUE;
		return;
	else if (data_presence[2] == FALSE)
	{
		strcpy(data[2], str);
		data_presence[2] = TRUE;
		return;
	}
}

static void initData()
{
	data[0] = (char *)malloc(DEFAULT_CAPACITY);
	data[1] = (char *)malloc(DEFAULT_CAPACITY);
	data[2] = (char *)malloc(DEFAULT_CAPACITY);
	data_presence[0] = FALSE;
	data_presence[1] = FALSE;
	data_presence[2] = FALSE;
}

static void resetData()
{
	free(data[0]);
	free(data[1]);
	free(data[2]);
	initData();
}
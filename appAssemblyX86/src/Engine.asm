; READY
proc ConvertFirstAtoI
  stdcall atoi, [ptrData0], [radix]
  mov [intData0], eax
  
  ret
endp

; READY
proc ConvertSecondAtoI  
  stdcall atoi, [ptrData2], [radix]
  mov [intData2], eax
    
  ret
endp

; READY
proc ConvertResItoA  
  stdcall itoa, [intRes], [radix], buffer, TRUE

  ret       
endp
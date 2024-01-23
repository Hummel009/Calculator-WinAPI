; READY
proc ConvertFirstAtoI
  mov ebx, 0
  mov ecx, 0
  
.cycle:
  cmp [storage0+ebx], '-'
  je .iterinc
  
  mov eax, dword[storage0+ebx]
  mov dword[localstorage0+ecx], eax
  inc ecx
  
  cmp ebx, 254
  je .exit
  
.iterinc:
  inc ebx
  jmp .cycle

.exit:
  cmp ebx, ecx
  jne .next
  
  stdcall atoi, localstorage0, [radix] 
  mov [intStorage0], eax   
  neg [intStorage0]

  ret

.next:
  stdcall atoi, localstorage0, [radix]
  mov [intStorage0], eax 

  ret
endp

; READY
proc ConvertSecondAtoI
  mov ebx, 0
  mov ecx, 0

.cycle:
  cmp [storage2+ebx], '-'
  je .iterinc
  
  mov eax, dword[storage2+ebx]
  mov dword[localstorage2+ecx], eax
  inc ecx
  
  cmp ebx, 254
  je .exit
  
.iterinc:
  inc ebx
  jmp .cycle

.exit:
  cmp ebx, ecx
  jne .next
    
  stdcall atoi, localstorage2, [radix]
  mov [intStorage2], eax  
  neg [intStorage2]
  
  ret

.next:  
  stdcall atoi, localstorage2, [radix]
  mov [intStorage2], eax

  ret
endp

; READY
proc ConvertResItoA  
  stdcall itoa, [intRes], [radix], buffer, TRUE

  ret       
endp
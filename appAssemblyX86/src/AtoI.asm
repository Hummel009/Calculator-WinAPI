;===============================================================================
;       ASCII to integer conversion procedure
;
;       input:
;       string - pointer to string
;       radix - base system like 10 for decimal, 16 for hexadecimal, etc.
;
;       output:
;       eax - number
;
;       possible errors:
;       radix exceeds 16
;       number is bigger than dword can store
;       digit is not 0...9 or A...F
;
;       if error occurs the value of eax is indefinable ;)
;
;       coded by Reverend // HTB + RAG
;===============================================================================
proc    atoi                    string, radix
        push    ebx ecx edx esi edi
        xor     ebx, ebx
        mov     ecx, [radix]
        mov     esi, [string]
        mov     edi, 1
        dec     [string]
        cld
        cmp     ecx, 16
        ja      .error
    @@:
        lodsb
        test    al, al
        jnz     @B
        sub     esi, 2
        std
  .loop:
        xor     eax, eax
        lodsb
        sub     al, '0'
        cmp     al, 9
        jbe     @F
        sub     al, 'A' - '9' - 1
    @@:
        cmp     al, 0Fh
        ja      .error
        imul    edi
        imul    edi, ecx
        add     ebx, eax
        cmp     edi, 10000000h
        ja      .error
        cmp     esi, [string]
        jnz     .loop
        mov     esi, [string]
        cmp     byte [esi], '-'
        jnz     @F
        neg     eax
    @@:
        mov     eax, ebx
        stc
  .theend:
        cld
        pop     edi esi edx ecx ebx
        ret
  .error:
        clc
        jmp     .theend
endp
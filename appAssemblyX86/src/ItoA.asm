;===============================================================================
;       integer to ASCII conversion procedure
;
;       input:
;       number - number to convert :)
;       radix - base system like 10 for decimal, 16 for hexadecimal, etc.
;       result_buffer - pointer to memory where output will be saved
;       signed - bool value, if number is signed (-2147483646...2147483647)
;                or unsigned (0...4294967295)
;
;       output:
;       no immediate output
;
;       possible errors:
;       radix exceeds 16
;
;       coded by Reverend // HTB + RAG
;===============================================================================
proc    itoa                    number, radix, result_buffer, signed
locals
  temp_buffer                   rb 32+1
endl
        pushad
        mov     esi, [radix]
        lea     edi, [temp_buffer+32]
        mov     ebx, itoa.digits
        cmp     esi, 16
        ja      .error
        std
        xor     al, al
        stosb
        mov     eax, [number]
        cmp     [signed], TRUE
        jnz     @F
        test    eax, 80000000h
        jz      @F
        neg     eax
    @@:
        xor     edx, edx
        idiv    esi
        xchg    eax, edx
        xlatb
        stosb
        xchg    eax, edx
        test    eax, eax
        jnz     @B
        lea     esi, [edi+1]
        mov     edi, [result_buffer]
        cld
        cmp     [signed], TRUE
        jnz     @F
        test    [number], 80000000h
        jz      @F
        mov     al, '-'
        stosb
    @@:
        lodsb
        stosb
        test    al, al
        jnz     @B
        sub     edi, [result_buffer]
        lea     eax, [edi-1]
        stc
  .theend:
        cld
        popad
        ret
  .error:
        clc
        jmp     .theend
endp
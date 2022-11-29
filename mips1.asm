.data # variable declarations follow this line # 数据变量声明
    array:
        .space 4000
    words:
    	.word 1, 2

.text # instructions follow this line


main: # indicates start of code (first instruction to execute)
    la $a0, words
    
    li $v0 10
    syscall
import isel.leic.utils.Time

object KBD {

    const val NONE = 0
    val KVAL_MASK = 0x01 //
    val K_MASK =  0x1E //0b_0001_1110
    val digits = arrayOf('1','4','7','*','2','5','8','0','3','6','9','#')
    val KACK_MASK = 0x80

    fun init(){
        HAL.clrBits(KACK_MASK)
    }

    fun getKey(): Char{
        if (HAL.isBit(KVAL_MASK)){
            val key = HAL.readBits(K_MASK)
            HAL.setBits(KACK_MASK)
            while (HAL.isBit(KVAL_MASK));
            HAL.clrBits(KACK_MASK)
            return digits[key shr(1)]
        }
        return  NONE.toChar()
    }

    fun waitKey(timeout: Long): Char{
        var key = NONE.toChar()
        val startTime = Time.getTimeInMillis()
        var endTime = Time.getTimeInMillis()
        while(endTime - startTime < timeout || key == NONE.toChar())
            endTime = Time.getTimeInMillis()
            key = getKey()
        return key
    }
}

fun main(){
    KBD.init()

    while(true){
        println(KBD.getKey())
    }
}
import isel.leic.UsbPort
//4  3  2  1  0
//k3 k2 k1 k0 kval

//KVAL_MASK = 0x01
//K_MASK = 0b_0001_1110 = 0x1E
//HAL.isBit(KVAL_MASK)
fun main(args: Array<String>) {
    val TestMask = 0x6E
    HAL.init()
    HAL.setBits(TestMask)
    HAL.clrBits(0x0F)
    HAL.writeBits(0x0F, 0x03)
    while (true){
        println(HAL.readBits(0xFF))
    }
    }
object HAL{
    var LastOutput = 0

    fun init(){
        LastOutput = 0
        UsbPort.write(LastOutput)
    }

    fun readBits(mask: Int): Int {
        return UsbPort.read() and mask
        }

    fun isBit(mask: Int): Boolean {
        val bits = readBits(mask)
        System.out.println(bits != 0)
        return bits != 0
    }

    fun setBits(mask: Int) {
        LastOutput = LastOutput or mask
        UsbPort.write(LastOutput)
    }

    fun clrBits(mask: Int){
        LastOutput = LastOutput and mask.inv() // lastoutput and not mask = 1
        UsbPort.write(LastOutput)
    }

    fun writeBits(mask: Int, value: Int) {
        LastOutput = LastOutput and mask.inv()
        setBits(value and mask)
    }
}

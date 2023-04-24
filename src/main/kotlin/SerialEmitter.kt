package main.kotlin

import HAL
import LCD

object SerialEmitter { // Sends frames for the different Serial Receiver modules.
    enum class Destination {LCD, DOOR}

    private var busy = false

    val MASK_SS = 0x20
    val MASK_SDX = 0x01
    val MASK_SCLK = 0x02

    // Initializes the class
    fun init() {
        HAL.setBits(MASK_SS)
        HAL.setBits(MASK_SCLK)
        HAL.setBits(MASK_SDX)
    }
    // Sends a frame with ‘data’ to the SerialReceiver identified as destination in ‘addr’
    fun send(addr: Destination, data: Int){
        var dataset = data
        HAL.clrBits(MASK_SS) //set SS to 0 – enable (hal MASK_EN = 0x20)

        HAL.clrBits(MASK_SCLK) // set clock to 0
        HAL.writeBits(MASK_SDX, dataset.takeLowestOneBit()) //D0
        HAL.setBits(MASK_SCLK) // set clock to 1

        HAL.clrBits(MASK_SCLK) // clear the clock
        dataset = dataset shr(1) // shift right 1 bit
        HAL.writeBits(MASK_SDX, dataset.takeLowestOneBit()) //D1
        HAL.setBits(MASK_SCLK) // set clock to 1

        HAL.clrBits(MASK_SCLK) // clear the clock
        dataset = dataset shr(1) // shift right 1 bit
        HAL.writeBits(MASK_SDX, dataset.takeLowestOneBit()) //D2
        HAL.setBits(MASK_SCLK) // set clock to 1

        HAL.clrBits(MASK_SCLK)
        dataset = dataset shr(1)
        HAL.writeBits(MASK_SDX, dataset.takeLowestOneBit()) //D3
        HAL.clrBits(MASK_SCLK)

        HAL.setBits(MASK_SS)
    }
    // Returns true if the series communication channel is busy
    fun isBusy(): Boolean {
        return busy
    }
}

fun main(){
    SerialEmitter.init()
    SerialEmitter.send(SerialEmitter.Destination.LCD, 0x05)

}
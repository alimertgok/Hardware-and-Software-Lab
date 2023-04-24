import isel.leic.utils.Time

object LCD { // Writes to the LCD using the 4-bit interface.
    private const val LINES = 2
    private const val COLS = 16; // Dimensions of the display.
    private const val SERIAL = false

    val MASK_RS = 0x10
    val MASK_DATA = 0x0F
    val MASK_EN = 0x20


    // Writes a command/data nibble to the LCD in parallel
    private fun writeNibbleParallel(rs: Boolean, data: Int) {
        if(!rs) HAL.writeBits(MASK_RS, 0) // or HAL.clearBits(MASK_RS)
        else HAL.writeBits(MASK_RS, MASK_RS) // or HAL.setBits(MASK_RS)
        HAL.setBits(MASK_EN)
        HAL.writeBits(MASK_DATA, data)
        HAL.clrBits(MASK_EN)
    }

    // Writes a command/data nibble to the LCD in series
    private fun writeNibbleSerial(rs: Boolean, data: Int) {
        if()
    }

    // Writes a command/data nibble to the LCD
    private fun writeNibble(rs: Boolean, data: Int) {
        if(SERIAL){
            writeNibbleSerial(rs, data)
        }
        else writeNibbleParallel(rs, data)
    }

    // Writes a command/data byte to the LCD
    private fun writeByte(rs: Boolean, data: Int) {
        writeNibble(rs, data shr(4) and 0xF)
        writeNibble(rs, data and 0xF)
    }
    // Writes a command to the LCD
    private fun writeCMD(data: Int) {
        writeByte(false, data)
    }
    // Writes data to the LCD
    private fun writeDATA(data: Int) {
        writeByte(true, data)
    }
    // Sends the initialization sequence for 4-bit communication.
    fun init() {
        Time.sleep(15)
        writeNibble(false, 0x03)
        Time.sleep(5)
        writeNibble(false, 0x03)
        Time.sleep(1/10)
        writeNibble(false, 0x03)
        writeNibble(false, 0x02)

        writeCMD(0x02)
        writeCMD(0x08)
        writeCMD(0x00)
        writeCMD(0x08)
        writeCMD(0x00)
        writeCMD(0x01)
        writeCMD(0x00)
        writeCMD(0x06)
    }

    // Writes a character at the current position.
    fun write(c: Char) {
        writeDATA(c.code)
        writeCMD(0x0F)
    }
    // Writes a string at the current position.
    fun write(text: String) {
        for(ch in text)
            write(ch)
    }
    // Sends a command to position the cursor (‘line’:0..LINES-1 , ‘column’:0..COLS-1)
    fun cursor(line: Int, column: Int) {
        val address = line * 0x40 + column
        writeCMD(0x80 or address)
    }
    // Sends a command to clear the screen and position the cursor at (0,0)
    fun clear() {
        writeCMD(0x01)
        cursor(0,0)
    }
}

fun main(){
    LCD.init()
    LCD.write("JULIA")
}
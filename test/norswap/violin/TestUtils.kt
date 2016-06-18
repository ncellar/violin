@file:Suppress("PackageDirectoryMismatch")
package norswap.violin.utils
import org.testng.annotations.Test
import org.testng.Assert.*

@Test fun charSequenceGet() {
    val str = "12345"
    assertEquals(str[0,-1], str)
    assertEquals(str[0, 2], "12")
    assertEquals(str[0,-2], "1234")
    assertEquals(str[2,-3], "3")
    assertEquals(str[2,-4], "")
    assertEquals(str[-4,-2], "234")
    assertEquals(str[3, 2], "")
    assertThrows { str[-str.length-1, -1] }
    assertThrows { str[0, str.length + 1] }
}

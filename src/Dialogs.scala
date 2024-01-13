import javax.swing.{JFrame, JOptionPane, JPasswordField}


/**
 * This class allows to display simple messages and have graphical interfaces to
 * enter words and characters.
 *
 * @version 2.0
 *
 */
object Dialogs {
  /**
   * This function open a dialog box to enter a hidden String. The dialog box
   * asks for a String containing a minimum of one character.
   *
   * @param message
   * The message displayed to ask for the hidden String
   * @return The hidden String entered
   */
  def getHiddenString(message: String): String = {
    val passwordField = new JPasswordField(10)
    val frame = new JFrame(message)
    // prompt the user to enter their name
    JOptionPane.showMessageDialog(frame, passwordField, message, JOptionPane.PLAIN_MESSAGE)
    val s = new String(passwordField.getPassword)
    if (s.length > 0) s
    else getHiddenString("Enter at least one character")
  }

  /**
   * This function open a dialog box to enter a character. This function
   * accepts only one character.
   *
   * @param message
   * The message asking for the character.
   * @return The character entered.
   */
  def getChar(message: String): Char = {
    val frame = new JFrame("Input a character please")
    val s = JOptionPane.showInputDialog(frame, message)
    if (s == null) System.exit(-1)
    if (s.length == 1) s.charAt(0)
    else getChar("Just one character")
  }

  def getString(message: String) : String = {
    var frame = new JFrame("Input 2 characters")
    var s = JOptionPane.showInputDialog(frame, message)


    return s
  }



  /**
   * Open a dialog box to display a message
   * @param message
   * The message to display.
   */
  def displayMessage(message: String): Unit = {
    JOptionPane.showMessageDialog(null, message, null, JOptionPane.PLAIN_MESSAGE)
  }
}

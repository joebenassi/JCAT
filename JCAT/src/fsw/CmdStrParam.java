package fsw;

public class CmdStrParam extends CmdParam
{

   /*
    * Constructor: String Parameter
    * 
    * NumBytes is the maximum length of teh string
    * 
    */
   public CmdStrParam(String Name, String DefValue, int NumBytes)
   {
      super (Name, ParamType.STR, DefValue, NumBytes);
      
   } // End CmdParam()
   
   @Override
protected byte[] loadByteArray() {
      
      System.out.println("CmdStrParam::loadByteArray with NumBytes = " + NumBytes); 

      // @todo - Add errror protection (null & invalid length)
      for (int i=0; i < Value.length(); i++) {
          ByteArray[i] = (byte)(Value.codePointAt(i) & 0x0FF);  // Unicode equals ASCII
          System.out.println("ByteArray["+i+"] = " + ByteArray[i]);
      }
      
      return ByteArray;
      
   } // End loadByteArray()

} // End Class CmdStrParam

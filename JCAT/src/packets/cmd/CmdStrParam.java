package packets.cmd;



public class CmdStrParam extends CmdParam
{

   /*
    * Constructor: String Parameter
    * 
    * NumBytes is the maximum length of teh string
    * 
    */
   public CmdStrParam(String Name, boolean isInputParam, String[] options, String DefValue, int NumBytes)
   {
      super (Name, isInputParam, options, ParamType.STR, DefValue, NumBytes);
      
   } // End CmdParam()
   
protected void loadByteArray() {
      
      System.out.println("CmdStrParam::loadByteArray with NumBytes = " + NumBytes); 

      //FIX ERROR BAD HERE GO TODO BYTE ARRAY
      // @todo - Add errror protection (null & invalid length)
      for (int i=0; i < Value.length(); i++) {
    	  try{
          ByteArray[i] = (byte)(Value.codePointAt(i) & 0x0FF);  // Unicode equals ASCII
    	  
          System.out.println("ByteArray["+i+"] = " + ByteArray[i]);
    	  } catch (Throwable e){e.printStackTrace();};
      }
      
   } // End loadByteArray()

} // End Class CmdStrParam

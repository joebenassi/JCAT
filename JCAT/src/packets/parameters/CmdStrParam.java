package packets.parameters;

public class CmdStrParam extends CmdParam
{
   public CmdStrParam(String Name, boolean isInputParam, ChoiceOption[] choiceOptions, int NumBytes)
   {
      super (Name, isInputParam, choiceOptions, ParamType.STR, NumBytes);
   }
   
protected void loadByteArray() {
      /* TODO fix this?? */
      /* TODO add error protection (null & invalid len. */
      
      for (int i=0; i < Value.length(); i++) {
    	  try{
    		  ByteArray[i] = (byte)(Value.codePointAt(i) & 0x0FF);  // Unicode equals ASCII
    		  System.out.println("ByteArray["+i+"] = " + ByteArray[i]);
    	  } catch (Throwable e){e.printStackTrace();};
      }
   }
}

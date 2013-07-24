package packets.parameters;

import utilities.EndianCorrector;

/*
** TODO - Think about adding Value also stored as an integer with a non string constructor
** TODO - More robust NumByte error handling
** TODO - Add radix support
*/
public class CmdIntParam extends CmdParam
{
      public CmdIntParam(String Name, boolean isInputParam, ChoiceOption[] choiceOptions, int NumBytes)
      {
         super (Name, isInputParam, choiceOptions, ParamType.INT, NumBytes);     
      }
      
      @Override
	protected void loadByteArray() {
         
         switch (ByteArray.length){
         
         case 1:
            System.out.println("CmdIntParam::loadByteArray - 1 byte parameter"); 
            ByteArray[0] = Integer.valueOf(Value).byteValue();
            System.out.println("ByteArray[0] = " + ByteArray[0]);
            break;
         
         case 2:
            System.out.println("CmdIntParam::loadByteArray - 2 byte parameter"); 
            int Temp2 = Integer.valueOf(Value);
            ByteArray[0] = (byte) (Temp2 & 0xFF);
            ByteArray[1] = (byte)((Temp2 & 0xFF00) >> 8);
            System.out.println("ByteArray[0] = " + ByteArray[0]);
            System.out.println("ByteArray[1] = " + ByteArray[1]);
            break;
         
         case 4:
            System.out.println("CmdIntParam::loadByteArray - 4 byte parameter"); 
            int Temp4 = Integer.valueOf(Value);
            ByteArray[0] = (byte) (Temp4 & 0x00FF);
            ByteArray[1] = (byte)((Temp4 & 0x0000FF00) >>  8);
            ByteArray[2] = (byte)((Temp4 & 0x00FF0000) >> 16);
            ByteArray[3] = (byte)((Temp4 & 0xFF000000) >> 24);
            System.out.println("ByteArray[0] = " + ByteArray[0]);
            System.out.println("ByteArray[1] = " + ByteArray[1]);
            System.out.println("ByteArray[2] = " + ByteArray[2]);
            System.out.println("ByteArray[3] = " + ByteArray[3]);
            break;

         default: System.out.println("CMDINTPARAM: Unsupported datasize"); 
         }  
         EndianCorrector.fixParameterOut(ByteArray);
      }
}
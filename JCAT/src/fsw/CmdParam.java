package fsw;

/*
** @todo - Add support for variable types, fields widths etc. Read-only?
** @author dmccomas
**
** Design Notes:
**   1. This class is intended to be used as an interface to a class providing a user interface such as
**      a GUI or a script. Therefore the user interface will be a string. On the 'back end' is a byte
**      array to the communications to the CFS. Since these interface types are fixed an abstract base
**      class can provide methods for the interfaces and the concrete subclasses would provide the conversion
**      implementation for each data type.
**   2. When I hard coded some defaults the string constructor seemed odd but if this project expands to use 
**      a text based database then it would also use text for the constructor so the design may be okay.
**   3. Generics didn't fit the problem being solved because they are typically used in containers when the
**      different types are being passed across a class interface.
*/
public abstract class CmdParam
{

   public enum ParamType { UNDEF, UINT, INT, STR };

   protected String    Name;
   protected ParamType Type;
   protected String    Value;
   protected int       NumBytes;    
   protected byte[]    ByteArray;
   
   /*
    * Constructor: Integer Parameter
    */
   public CmdParam(String Name, ParamType  Type, String DefValue, int NumBytes)
   {
      this.Name = Name;
      this.Type = Type;
      this.Value    = DefValue;
      this.NumBytes = NumBytes;
      ByteArray = new byte[NumBytes];
      loadByteArray();
      
   } // End CmdParam()
   
   public String getName() {
      return Name;
   }

   public void setName(String Name) {
      this.Name = Name;
   }

   public ParamType getType() {
      return Type;
   }

   /*
    *  Each subclass type must provide the conversions for the
    *  particular parameter type
    */
   protected abstract byte[] loadByteArray();   // Load byte array using current Value. Intent is an internal helper function
  

   public byte[] getByteArray() {
      
      return ByteArray;
   
   } // End getByteArray

   public byte[] setValue(String value) {
      
      Value = value;
      return loadByteArray();
      
   } // End setValue 
  

   /*
    *  Can only get the number of bytes and not set them. It should never change once
    *  the parameter is created.
    */
   
   public int getNumBytes() {
      return NumBytes;
   }

} // End class CmdParam

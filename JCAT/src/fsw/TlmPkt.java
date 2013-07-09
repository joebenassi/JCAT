// @todo - Add constructor with TlmPt list
// @todo - Add build all telemetry packets from an XML file
// @todo - Add function to create table of data that can be easily displayed

package fsw;

import java.util.ArrayList; 

public class TlmPkt
{

   private String  Name;
   private int     MsgId;

   private ArrayList<TlmPnt> TlmPntList = new ArrayList<TlmPnt>();
   
   public TlmPkt (String Name, Integer MsgId) {
   
      this.Name  = Name;
      this.MsgId = MsgId;
      
   } // End TlmPkt()
   

   public String getName()
   {      
      return Name;
   
   } // getName()

   public void addTlmPnt(TlmPnt tlm)
   {
      TlmPntList.add(tlm);
   }
   
   public ArrayList<TlmPnt> getTlmPntList()
   {      
      return TlmPntList;
   
   } // getTlmPntList()
   
} // End class TlmPkt

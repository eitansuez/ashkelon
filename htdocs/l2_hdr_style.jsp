<%@ page info="dynamic style?" import="java.util.*,org.ashkelon.util.*,org.ashkelon.db.*,org.ashkelon.*" %>

<%
  String selectedColor = ServletUtils.getRequestParam(request, "selected_color");
  String unselectedColor = ServletUtils.getRequestParam(request, "unselected_color");

  if (selectedColor == null || selectedColor.equals(""))
  {
    selectedColor = "#92A6F5";
  }
  if (unselectedColor == null || unselectedColor.equals(""))
  {
    unselectedColor = "#CDCDCD";
  }
%>

<style>
  TABLE.tab2
  {
    background-color: <%=selectedColor%>;
    border: 1px solid black;
    border-collapse: collapse;
    border-top: 0 none white;
    border-right: 0 none white;
    border-left: 0 none white;
  }
  
  TD.tab_buffer2
  {
    background-color: white;
    width: 5 px;
    border: 1px solid black;
    border-top: 0 none white;
    padding: 0;
  }
  
  TD.tab_selected_tab2
  {
    padding: 3 10 3 10;
    background-color: <%=selectedColor%>;
    border: 1px solid black;
    border-bottom: 1px solid <%=selectedColor%>;
    cursor: default;
    font-weight: bold;
  }
  
  TD.tab_tab2
  {
    padding: 3 10 3 10;
    background-color: <%=unselectedColor%>;
    border: 1px solid black;
    cursor: pointer;
  }
  
  TD.tab_pad2
  {
    width: 20 px;
    background-color: white;
    border-bottom: 1px solid black;
    padding: 0;
    border-top: 0 none white;
    border-right: 0 none white;
    border-left: 0 none white;
  }
</style>

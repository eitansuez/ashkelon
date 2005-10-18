<% if (!("/search.jsp".equals(request.getServletPath()))) { %>

<div id="search-callout">
  <form id="search-form" name="search_form" method="post" action="search.do">
    <input type="hidden" name="simple" value="true" />
    <input type="hidden" name="srch_type" value="class">

    <label for="searchField" accesskey="s">Class <u>S</u>earch: </label>
    <input type="text" id="searchField" name="searchField" size="25" maxlength="100" style="background-color: beige;" />

    <input type="submit" value="Go" /> <!-- safari does not like other method of using a button tag for form submission -->
  </form>
</div>

<% } %>

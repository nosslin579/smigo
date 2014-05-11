<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<style>
    #mainmenu .mainmenuitem:nth-of-type(5) {
        background: #466919 !important;
    }
</style>

<jsp:include page="header.jsp"/>

<div id="help" class="smigoframe largecenteredsmigoframe">
    <div class="smigoframeheader"><spring:message code="help"/></div>
    <div class="smigoframecontent">

        <div class="faq"><spring:message code="general.faq"/></div>

        <div class="questioncontainer">
            <div class="questionquestion">What is this?</div>
            <div class="questionanswer">-Smigo is a garden planner for vegetables and flowers. It will give you advice about companion planting and
                crop rotation.
            </div>
        </div>

        <div class="questioncontainer">
            <div class="questionquestion">How does it work?</div>
            <div class="questionanswer">-First you need to signup and login. Click on a species on the left menu and then somewhere in the grid. Now
                you have added your first plant. Add more species and items so your grid equals your garden. Click "Add year" to plan the next year.
            </div>
        </div>

        <div class="questioncontainer">
            <div class="questionquestion">How do I delete a plant?</div>
            <div class="questionanswer">-Hold shift and click square or select the delete plant and click the square.</div>
        </div>

        <div class="questioncontainer">
            <div class="questionquestion">Can you translate the page into my language?</div>
            <div class="questionanswer">-You can do that yourself. It is easy and only takes an hour. Send me an <a
                    href="mailto:christian1195@gmail.com">email</a>.
            </div>
        </div>

        <div class="questioncontainer">
            <div class="questionquestion">Is it for free?</div>
            <div class="questionanswer">-Yes it is. And tell all your friends about it.</div>
        </div>

        <div class="questioncontainer">
            <div class="questionquestion">My question is not listed here. What do I do?</div>
            <div class="questionanswer">-You can ask at <a href="http://www.facebook.com/smigogarden">Facebook</a> or send me an <a
                    href="mailto:christian1195@gmail.com">email</a></div>
        </div>


    </div>
</div>

<jsp:include page="footer.jsp"/>
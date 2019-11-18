function getData(page) {
    var minPrice = document.getElementById("minPriceFilter").value;
    var maxPrice = document.getElementById("maxPriceFilter").value;
    var discount = document.getElementById("discountCheckbox").checked;
    var filter = false;

    if (minPrice !== "" || maxPrice !== "" || discount !== "") {
        filter = true;
    }

    var data = "&page=" + encodeURIComponent(page) + "&filter=" + encodeURIComponent(filter)
        + "&minPrice=" + encodeURIComponent(minPrice) + "&maxPrice=" + encodeURIComponent(maxPrice)
        + "&discount=" + encodeURIComponent(discount);

    xhrSender("/get", data, drawTable);
}

function drawTable(obj) {
    var count = obj.list.length;

    document.getElementById("dish-table").getElementsByTagName('tbody')[0].innerHTML = "";
    var placeToInsert = document.getElementById("dish-table").getElementsByTagName('tbody')[0];
    var htmlInsert = "";

    if (count > 0) {
        for (var i = 0; i < count; i++) htmlInsert += fillTrHtml(obj.list[i].id, obj.list[i].title,
            obj.list[i].weight, obj.list[i].price, obj.list[i].discountAmount);
    } else {
        htmlInsert = '<tr><td colspan="5" class="text-center">No results match your search criteria!</td></tr>';
    }
    placeToInsert.insertAdjacentHTML("afterbegin", htmlInsert);

    drawPagesCounter(obj);
}

function fillTrHtml(id, title, weight, price, discountAmount) {
    if (discountAmount === 0) {
        discountAmount = "-";
    }

    return '<tr>' +
        '<td><button type="button" class="btn btn-outline-primary btn-sm" style="height:28px;width:28px;' +
        'padding-left:6px;padding-top:3px;" item="' + id + '"><span class="oi oi-cart"></span></button></td>' +
        '<td>' + title + '</td>' +
        '<td>' + weight + '</td>' +
        '<td>' + price + '</td>' +
        '<td>' + discountAmount + '</td>' +
        '</tr>';
}

function fillModalShopCartHtml(id, title, weight, price, discountAmount, total) {
    return '<tr>' +
        '<td>' + id + '</td>' +
        '<td>' + title + '</td>' +
        '<td>' + weight + '</td>' +
        '<td>' + price + '</td>' +
        '<td>' + discountAmount + '</td>' +
        '<td>' + total + '</td>' +
        '</tr>';
}

function drawPagesCounter(obj) {
    var pages = obj.pagesCount;
    document.getElementById("dish-pages").innerHTML = "";

    if (pages > 0) {
        for (var i = 0; i < pages; i++) {

            var htmlInsert = '<li class="page-item"><a class="page-link" href="#">' + (i + 1) + '</a></li>';

            var placeToInsert = document.getElementById("dish-pages");
            placeToInsert.insertAdjacentHTML('beforeend', htmlInsert);

            if (i === obj.currentPage) {
                document.getElementsByClassName("page-item")[i].classList.add("active");
            }
        }
    }
}

function xhrSender(path, req, func) {
    var xhr = new XMLHttpRequest();
    xhr.open("post", path, true);
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.responseType = "json";
    xhr.send(req);
    xhr.onload = function () {
        if (xhr.status !== 200) {
            drawErrorPopup("Please make sure all required fields are filled out correctly!");
        } else {
            func(xhr.response);
        }
    }
}

function drawErrorPopup(html) {
    $('#closeModal').click();
    $('div.toast-header').addClass("bg-danger");
    $('div.toast-body').html(html);
    $('.toast').toast('show', 'delay');
}

function clearModalInputs() {
    $('#inputDishName').val('');
    $('#inputDishWeight').val('');
    $('#inputDishPrice').val('');
    $('#inputDishDiscount').val('');
}

function updateTotalWeightIndicator(weight) {
    document.getElementById("totalWeight").getElementsByTagName("span")[1].innerText = weight;
}

function getItemFromTable(selector, table) {

    var itemId = selector.attr("item");
    var itemTitle = table.find("td:nth-child(2)").text();
    var itemWeight = table.find("td:nth-child(3)").text();
    var itemPrice = table.find("td:nth-child(4)").text();
    var itemDiscount = table.find("td:nth-child(5)").text();
    var totalPrice;

    if (itemDiscount !== "-") {
        itemPrice = parseFloat(itemPrice);
        itemDiscount = parseInt(itemDiscount);
        totalPrice = itemPrice * (100 - itemDiscount) / 100;
    } else {
        totalPrice = itemPrice;
    }

    return {
        id: itemId,
        title: itemTitle,
        weight: itemWeight,
        price: itemPrice,
        discount: itemDiscount,
        total: totalPrice
    };
}

function addDish(response) {
    var title = $("#inputDishName").val();
    var weight = $("#inputDishWeight").val();
    var price = $("#inputDishPrice").val();
    var discountAmount = $("#inputDishDiscount").val();

    $("div.toast-header").removeClass("bg-danger").addClass("bg-primary");

    if (response.response === "0") {
        $("#closeModal").click();
        $("div.toast-body").html("Successfully added <b>" + title + "</b>");
        $(".toast").toast('show', 'delay');
        clearModalInputs();
        var htmlInsert = fillTrHtml(response.id, title, weight, price, discountAmount);
        var count = $("#dish-table > tbody > tr").length;
        $("#dish-table > tbody > tr").eq(count - 1).remove();
        $("#dish-table > tbody > tr").eq(0).before(htmlInsert);
    } else if (response.response === "2") {
        $("#closeModal").click();
        $("div.toast-header").addClass("bg-danger");
        $("div.toast-body").html("Something went wrong!");
        $(".toast").toast('show', 'delay');
        clearModalInputs();
    }
}

function createShoppingCartTable() {
    var orderSesStor = window.sessionStorage.order;
    var order;

    try {
        order = JSON.parse(orderSesStor);
    } catch (error) {
        //console.error(error);
    }

    document.getElementById("shopping-cart-table").getElementsByTagName("tbody")[0].innerHTML = "";
    var placeToInsert = document.getElementById("shopping-cart-table").getElementsByTagName('tbody')[0];
    var htmlInsert = "";

    if (order) {
        var totalPrice = 0;
        var totalWeight = 0;

        for (var i = 0; i < order.length; i++) {
            var id = order[i].id;
            var title = order[i].title;
            var weight = order[i].weight;
            var price = order[i].price;
            var discount = order[i].discount;
            var total = order[i].total;

            totalWeight += parseInt(weight);
            totalPrice += parseFloat(total);
            htmlInsert += fillModalShopCartHtml(id, title, weight, price, discount, total);
        }

        htmlInsert += '<tr><td colspan="2"></td><td class="text-left"><b>' + totalWeight
            + '</b></td><td colspan="2"></td><td class="text-left"><b>' + totalPrice.toFixed(2) + '</b></td></tr>';

    } else {
        htmlInsert = '<tr><td colspan="6" class="text-center">Empty!</td></tr>';
    }

    placeToInsert.insertAdjacentHTML('afterbegin', htmlInsert);
}

$('#dish-table').on('click', '.btn', function () {
    var orderSesStor = window.sessionStorage.order;
    var table = $(this).closest("tr");
    var order;
    var sumWeight = 0;
    var itemWeight;
    var totalWeight;
    var itemInfo;

    try {order = JSON.parse(orderSesStor);
    } catch (error) {/*console.error(error);*/}

    if (order) {
        for (var i = 0; i < order.length; i++) {
            sumWeight += parseFloat(order[i].weight);
        }

        itemWeight = parseFloat(table.find("td:nth-child(3)").text());
        totalWeight = sumWeight + itemWeight;

        if (totalWeight <= 1000) {
            itemInfo = getItemFromTable($(this), table);
            updateTotalWeightIndicator(totalWeight);

            order.push(itemInfo);
            window.sessionStorage.order = JSON.stringify(order);
        } else {
            drawErrorPopup("Maximum weight is 1 kg!");
        }

    } else {
        order = [];
        itemInfo = getItemFromTable($(this), table);
        itemWeight = itemInfo.weight;

        updateTotalWeightIndicator(itemWeight);

        order.push(itemInfo);
        window.sessionStorage.order = JSON.stringify(order);
    }
});

$('#saveDishButton').on( "click", function () {
    var title = $("#inputDishName").val();
    var weight = $("#inputDishWeight").val();
    var price = $("#inputDishPrice").val();
    var discountAmount = $("#inputDishDiscount").val();

    var highlight = false;
    if (title === "") {
        $("#inputDishName").addClass("border-danger");
        highlight = true;
    }
    if (weight === "") {
        $("#inputDishWeight").addClass("border-danger");
        highlight = true;
    }
    if (price === "") {
        $("#inputDishPrice").addClass("border-danger");
        highlight = true;
    }
    if (discountAmount === "") {
        $("#inputDishDiscount").addClass("border-danger");
        highlight = true;
    }
    if (highlight) {
        return;
    }

    var data = "title=" + encodeURIComponent(title) + "&weight=" + encodeURIComponent(weight) + "&price=" + encodeURIComponent(price) + "&discountAmount=" + encodeURIComponent(discountAmount);
    xhrSender("/add_dish", data, addDish);
});

$('#dish-pages').on('click', '.page-item', function () {
    var $page = $(this).children().text() - 1;
    getData($page);
});

$('#shopping-cart-button').click(function () {
    createShoppingCartTable();
});

$("#inputDishName").click(function () {
    $("#inputDishName").removeClass("border-danger");
});

$("#inputDishWeight").click(function () {
    $("#inputDishWeight").removeClass("border-danger");
});

$("#inputDishPrice").click(function () {
    $("#inputDishPrice").removeClass("border-danger");
});

$("#inputDishDiscount").click(function () {
    $("#inputDishDiscount").removeClass("border-danger");
});

$("#priceFilterBtn").click(function () {
    getData(0);
});

window.onload = function () {
    setTimeout(function () {
        window.sessionStorage.order = "";
        getData(0);
    }, 50);
}
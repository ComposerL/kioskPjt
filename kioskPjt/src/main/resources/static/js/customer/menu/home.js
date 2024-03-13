$(document).ready(function() {
        
    $(".openModal").click(function() {
        $("#myModal").fadeIn();
    });

     $(".close, #myModal").click(function(e) {
        if (e.target === this) {
            $("#myModal").fadeOut();
            resetValues();
        }
    });
  
    $(".select_temper button:nth-child(2)").click(function(){
        $(this).css('border','2px solid #fff').css('backgroundColor','blue').css('color','#fff');
        $(".select_temper button:nth-child(1)").css('border','1px solid red').css('backgroundColor','inherit').css('color','red');
    });

    $(".select_temper button:nth-child(1)").click(function(){
        $(this).css('border','2px solid #fff').css('backgroundColor','red').css('color','#fff');
        $(".select_temper button:nth-child(2)").css('border','1px solid blue').css('backgroundColor','inherit').css('color','blue');
    });

    $(".ice_poti button:nth-child(1)").click(function(){
        $(this).css('border', '2px solid #fff').css('backgroundColor', 'blue').css('color', '#fff');
        $(".ice_poti button:nth-child(2)").css('border', '2px solid blue').css('backgroundColor', 'inherit').css('color', 'blue');
    });

    $(".ice_poti button:nth-child(2)").click(function(){
        $(this).css('border', '2px solid #fff').css('backgroundColor', 'blue').css('color', '#fff');
        $(".ice_poti button:nth-child(1)").css('border', '2px solid blue').css('backgroundColor', 'inherit').css('color', 'blue');
    });

    $(".select_oreder input[name='cancel']").click(function(){
        $(this).css('border', '2px solid #fff').css('backgroundColor', 'blue').css('color', '#fff');
        $(".select_oreder input[name='select']").css('border', '2px solid blue').css('backgroundColor', 'inherit').css('color', 'blue');
    });

    $(".select_oreder input[name='select']").click(function(){
        $(this).css('border', '2px solid #fff').css('backgroundColor', 'blue').css('color', '#fff');
        $(".select_oreder input[name='cancel']").css('border', '2px solid blue').css('backgroundColor', 'inherit').css('color', 'blue');
    });
    
     menuSelectHandler();
    
});

function menuPrice() {
	let menuPrice = $('p.menu_price').attr('fc_menu_price');
	console.log("menuPrice: ", menuPrice);
	return menuPrice;
}

	// 초기화 함수
    function resetValues() {
        $("p.menu_number").text("1"); // 메뉴 수량 초기화
        $("p.shot_number").text("0"); // 샷 수량 초기화
        $("p.pur_number").text("0"); // 퍼 수량 초기화
        const shotPrice = 500.0000;
        const purPrice = 1000.0000;
        $(".menu_price").text(menuPrice.toLocaleString('ko-KR')); // 메뉴 가격 초기화
        $(".shot_price").text(shotPrice.toLocaleString('ko-KR')); // 샷 가격 초기화
        $(".pur_price").text(purPrice.toLocaleString('ko-KR')); // 퍼 가격 초기화
        
    }

function menuSelectHandler() {
    const shotPrice = 500;
    const purPrice = 1000;

    // 초기 수량 설정
    let menuCurrentValue = 1;
    let shotCurrentValue = 0;
    let purCurrentValue = 0;

    // 가격 업데이트 함수
    function updatePrice() {
		
        const totalPrice = (menuPrice() * menuCurrentValue) + (shotPrice * shotCurrentValue) + (purPrice * purCurrentValue);
        $(".menu_price").text(totalPrice);
        console.log("totalPrice: "+totalPrice);
    }

    // 초기화 함수
    /*
    function resetValues() {
        menuCurrentValue = 1;
        shotCurrentValue = 0;
        purCurrentValue = 0;

        $(".menu_number").text(menuCurrentValue);
        $(".shot_number").text(shotCurrentValue);
        $(".pur_number").text(purCurrentValue);

        updatePrice();
    }
    */

    // 모달 닫힐 때 초기화
    $('#myModal').on('hidden.bs.modal', function () {
        resetValues();
    });




    // 초기화
    // resetValues();

    // 메뉴 수량 버튼
    $(".menu_add").off("click").on("click", function() {
        menuCurrentValue++;
        $(".menu_number").text(menuCurrentValue);
        updatePrice();
    });

    $(".menu_min").off("click").on("click", function() {
        if (menuCurrentValue > 1) {
            menuCurrentValue--;
            $(".menu_number").text(menuCurrentValue);
            updatePrice();
        }
    });

    // 샷 수량 버튼
    $(".shot_add").off("click").on("click", function() {
        shotCurrentValue++;
        $(".shot_number").text(shotCurrentValue);
        updatePrice();
    });

    $(".shot_min").off("click").on("click", function() {
        if (shotCurrentValue > 0) {
            shotCurrentValue--;
            $(".shot_number").text(shotCurrentValue);
            updatePrice();
        }
    });

    // 퍼 수량 버튼
    $(".pur_add").off("click").on("click", function() {
        purCurrentValue++;
        $(".pur_number").text(purCurrentValue);
        updatePrice();
    });

    $(".pur_min").off("click").on("click", function() {
        if (purCurrentValue > 0) {
            purCurrentValue--;
            $(".pur_number").text(purCurrentValue);
            updatePrice();
        }
    });
}
;(function (win, $) {
  'use strict';

  var editBox = function(){
    $('.btn_order').on('click',function(){
      $(this).hide();
      $(this).siblings('.btn_save').show();
      $('.my_menu_panel').find('.my_menu').addClass('edit');

      checkEditMode();
      sortingMenu(this);
    })

    $('.btn_save').on('click',function(){
      $(this).hide();
      $(this).siblings('.btn_order').show();
      $('.my_menu_panel').find('.my_menu').removeClass('edit');
      checkEditMode();
      sortingMenu(this);
    })
  }

  //메뉴 편집 드래그 & 슬라이드 스와이프 모션 충돌 방지
  var checkEditMode = function(){
    if($('.my_menu').hasClass('edit')){
      $('.my_menu_panel').addClass('swiper-no-swiping');
    } else{
      $('.my_menu_panel').removeClass('swiper-no-swiping');
    }
  }

  var sortingMenu = function(btnType){
    if(btnType.text === '순서변경'){
      $(".my_menu").sortable({
        items: "> li"
      });
    } else if(btnType.text === '저장'){
      $(".my_menu").sortable( "destroy");
    }
  }

  $('.scroll_inner').on('scroll', function(){
      var $win_scroll = $(this).scrollTop();

      $('.header').addClass('fixed');

      if($win_scroll <= 20){
        $('.header').removeClass('fixed');return false;
      }
  })

  var layer = function() {
    $('.js_popup_close').on('click', function(){
      $.scrollLock(false);
      $('.layer').removeClass('on toast');
      $('.popup').hide();
    });

    $('.dimmed').on('click', function(){
      $.scrollLock(false);
      $('.layer').removeClass('on toast');
      $('.popup').hide();
    });

    var scrollLock = function() {
      if ($('.layer').hasClass('on')) {
        $.scrollLock(true);
      } else {
        $.scrollLock(false);
      }
    }
    scrollLock();
  }

  //메인 슬라이
  var mainMenu = [
    '알림',
    '나의 메뉴',
    '오늘의 이슈',
    '모바일 신분증'
  ]
  var mainSwiper = new Swiper ('.swiper-container', {
    pagination: {
      el: '.swiper-pagination',
      clickable : true,
      renderBullet : function ( index ,className ) {
        return '<li class="' + className + '"><a href="#">' + ( mainMenu[ index ] ) +'</a></li>';
			}
    }
  })

  var gotoSlidePage = function(){
      mainSwiper.slideTo(3);
  }

  var scrollToSlide = function () {
    if (window.location.href.indexOf("#mobile") > -1) {
      var winLocation = window.location.href,
        winLocationSplit = winLocation.split('html')[1];
      }
      if(winLocationSplit != null){
        gotoSlidePage();
      }
  };


  var accordion = function () {

    $('.lst_inbox .item').each(function () {
      var hTit = $(this).find('.tit_box').innerHeight();
      var hCont = $(this).find('.txt').outerHeight(true);

      if($(this).find('.txt').length!=0){
        $(this).css('height',hCont+hTit);
      } else{
        $(this).css('height', hTit);
      }
    })

    $('.lst_inbox .tit_box').on('click', function () {
      var $this = $(this),
          $parent = $this.parent();
      var hTit = $this.innerHeight();

      if($parent.hasClass('is_expanded')) {
        if($this.next().find('.txt').length!=0){
          $parent.addClass('is_closed').css('height',51+hTit);
        }
        else{
          $parent.addClass('is_closed').css('height',hTit);
        }
        var timeDelay = setTimeout(function() {
          $parent.removeClass('is_expanded');
        }, 300);
        return;
      }

      $('.lst_inbox .item').each(function () {
        var hTit = $(this).find('.tit_box').innerHeight();

        if($(this).find('.txt').length!=0){
          $(this).addClass('is_closed').css('height',51+hTit);
        } else{
          $(this).addClass('is_closed').css('height',hTit);
        }
        $(this).removeClass('is_expanded');
      })

      $parent.addClass('is_expanded').css('height',$parent.prop('scrollHeight'));
    });
  };

  /*js select*/
  var selectBox = function(){
    var $selbox = $('.sel_box');
    $selbox.find('select').change(function(){
      var selectedItem = $(this).children("option:selected").text();
      $selbox.find('button').text(selectedItem);
    });
  }

  /*js input*/
  var eventInputPlaceholder = function (){
    var $input_txt = $('.input_area input');
    $input_txt.focus(function(){
      $(this).parent().addClass('is_focus');
    });
    $input_txt.focusout(function(){
      $('.input_area').removeClass('is_focus');
    })
  }

  $(win).on('load', function () {
    editBox();
    layer();
    eventInputPlaceholder();
    scrollToSlide();
    accordion();
    selectBox();
    // slideshowToggle();
  });

})(window, window.jQuery);
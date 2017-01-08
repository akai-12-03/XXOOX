define(function(require){
	var timelimitLblArray = ['月', '天'];
	$('input[name=isDay]').on('change', function(){
		var val = $(this).val();
		$('label[for=timelimit]').text(timelimitLblArray[val]);

		// 还款方式
		if(val === '1'){
			$('#repayment_style').find('option[value=1],option[value=3]').hide();
			$('#repayment_style>option[value=2]').prop('selected', true);
		}
		else if(val === '0'){
			$('#repayment_style').find('option[value=1],option[value=3]').show();
		}
	}).first().trigger('click');


	$('input[name=award]').on('change', function(){
		var val = $(this).val();
		if(val === '1'){
			$('.award-group').first().stop(true, true).slideUp('slow');
			$('.award-group').last().stop(true, true).slideDown('slow');
		}
		else if(val === '2'){
			$('.award-group').last().stop(true, true).slideUp('slow');
			$('.award-group').first().stop(true, true).slideDown('slow');
		}
		else if(val === '0'){
			$('.award-group').stop(true, true).slideUp('slow');
		}

	}).first().trigger('click');

	$('input[name=ispwd]').on('change', function(){
		var val = $(this).val();
		if(val === '1'){
			$('.pwd-group').stop(true, true).slideDown('slow');
			$('#pwd').attr('required', true);
		}
		else if(val === '0'){
			$('.pwd-group').stop(true, true).slideUp('slow');
			$('#pwd').removeAttr('required');
		}
	});

	$('#get-suggest').on('click', function(){
		var $this = $(this);

		var box = $this[0].getBoundingClientRect();
		var text = $.trim($('#username').val());
		var listNodeTpl = '<div class="list-node" data-id="{$id}">{$name}</div>';
		$('#suggest-list').on('click', '.list-node', function(){
			var text = $(this).text();
			var id = $(this).data('id');
			$('#username').val(text);
			$('#borrower').val(id);
			$('#suggest-list').slideUp(1500, function(){
				$(this).empty();
			});
		});
		$.ajax({
			'url': urlConfig.bnsearch,
			'type': 'get',
			'dataType': 'json',
			'data': {
				'username': text
			},
			success: function(res){
				if(res.status === 1 && res.data.length > 0){
					var html = '';
					for(var i=0,len=res.data.length;i<len;i++){
						html += listNodeTpl.replace('{$name}', res.data[i]['name'])
							.replace('{$id}', res.data[i]['id']);
					}

					$('#suggest-list').html(html).css({
						'left': box.left + box.width + 10,
						'top': box.top + window.scrollY
					}).slideDown('slow');
				}
			},
			error: function(err){
				console.log(err);
			}
		})
	});

});
seajs && seajs.config({
	base: ROOT_URL + "/dist/js/",

	map: [
		// [".js", ".min.js"]
	],

	vars: {
		"libs": ROOT_URL + "/dist/lib"
	},

	alias: {
		'jquery': ROOT_URL + '/dist/sea-modules/jquery',
		'es5-safe': ROOT_URL + '/dist/ie-fixed/es5-safe',
		'jquery-easyui': ROOT_URL + '/dist/jeasyui/jquery.easyui.min',
		'pretty-photo': ROOT_URL + '/dist/lib/jquery.prettyPhoto',

		'rich-editor': ROOT_URL + '/dist/kindeditor/kindeditor-min.js',
		'input-patch': ROOT_URL + '/dist/css/borrow-input.min.css',
		'fancybox-mousewheel': ROOT_URL + '/dist/fancybox/jquery.mousewheel-3.0.6.pack',
		'fancybox': ROOT_URL + '/dist/fancybox/jquery.fancybox',
		'fancybox-buttons': ROOT_URL + '/dist/fancybox/helpers/jquery.fancybox-buttons',
		'raphael': ROOT_URL + '/dist/lib/raphael',

		'form': 'modules/form',
		'validator': 'modules/validator',
		'form-validator': 'modules/form-validator',
		'accordion': 'modules/accordion',
		'verify-tel': 'modules/verify-tel',
		'verify-img': 'modules/verify-img',
		'editor': 'modules/editor',
		'panel': 'modules/panel',
		'dialog': 'modules/dialog',
		'ring': 'modules/ring'
	},

	preload: [
		// "jquery",
		Function.prototype.bind ? "" : "es5-safe",
	]
});
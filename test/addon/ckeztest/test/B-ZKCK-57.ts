/* B-ZKCK-57.ts

	Purpose:
		
	Description:
		
	History:
		Wed Oct 14 17:08:00 CST 2020, Created by rudyhuang

Copyright (C) 2020 Potix Corporation. All Rights Reserved.
*/
import { Selector } from 'testcafe';
import * as assert from 'assert';

fixture('ZKCK-57')
	.page('http://localhost:8080/ckeztest/test2/B-ZKCK-57.zul');

test('test', async t => {
	// https://testcafe.io/documentation/402670/reference/test-api/domnodestate
	const div = await Selector('.z-div')(),
		ckeditor = await Selector('.z-ckeditor')();
	assert.ok(div.clientHeight, '`clientHeight` of ".z-div" is undefined');
	assert.ok(ckeditor.offsetHeight, '`offsetHeight` of ".z-ckeditor" is undefined');
	assert.ok(ckeditor.offsetHeight >= div.clientHeight, 'ckeditor is smaller than div');
});
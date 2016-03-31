package com.zhidian3g.common.log;

import org.slf4j.LoggerFactory;

import com.zhidian3g.common.utils.JsonUtil;

import akka.actor.UntypedActor;

public class WinLoggerHander extends UntypedActor {

	@Override
	public void onReceive(Object msg) {
		LoggerFactory.getLogger("cyWinLog").info(JsonUtil.bean2Json(msg));
	}

}

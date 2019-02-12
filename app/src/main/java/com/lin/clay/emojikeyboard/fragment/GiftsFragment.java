package com.lin.clay.emojikeyboard.fragment;


import com.lin.clay.emojikeyboard.MainActivity;
import com.lin.clay.emojikeyboard.model.IMChatGiftsModel;

public class GiftsFragment extends GiftsBaseFragment
{
	/**
	 * 请求网络获取礼物信息
	 */
	@Override
	public void requestGistsInfo()
	{
		((MainActivity)getActivity()).loadChatGifts();
	}

	/**
	 * 处理选中的礼物
	 *
	 * @param checkGifts
	 */
	@Override
	public void disposeSelectedGift(IMChatGiftsModel.GiftDetail checkGifts)
	{
		((MainActivity)getActivity()).gradualGifts(checkGifts);
	}
}
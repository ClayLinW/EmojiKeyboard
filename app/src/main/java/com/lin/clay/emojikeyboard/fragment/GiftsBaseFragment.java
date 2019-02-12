package com.lin.clay.emojikeyboard.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.lin.clay.emojikeyboard.R;
import com.lin.clay.emojikeyboard.adapter.EmotionGiftsFragmentAdapter;
import com.lin.clay.emojikeyboard.adapter.HorizontalRVAdapter;
import com.lin.clay.emojikeyboard.model.EmotionGiftsCheckedModel;
import com.lin.clay.emojikeyboard.model.IMChatGiftsModel;
import com.lin.clay.emojikeyboard.utils.FragmentFactory;
import com.lin.clay.emojikeyboard.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

public abstract class GiftsBaseFragment extends Fragment
{

	private ViewPager vpGifts;
	private RecyclerView rvGifts;
	private RelativeLayout rlGradualGifts;

	//当前表情被选中底部tab,记录底部默认选中第一个
	private int CurrentPosition = 0;

	List<Fragment> giftsFragments = new ArrayList<>();
	private HorizontalRVAdapter rvGiftsAdapter;
	private List<EmotionGiftsCheckedModel> giftsCheckedList;
	private boolean mIsLoadGiftSuccess;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.chat_gifts_fragment, container, false);
		vpGifts = view.findViewById(R.id.vp_gifts);
		rvGifts = view.findViewById(R.id.rv_gifts);
		rlGradualGifts = view.findViewById(R.id.rl_gradualGifts);
		return view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		requestGistsInfo();
		rlGradualGifts.setEnabled(false);
		rlGradualGifts.setAlpha(0.3f);
		rlGradualGifts.setOnTouchListener(Utils.getTouchBackListener(0.8f));
		rlGradualGifts.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				sendGifts();
			}
		});
	}

	public void setNeedClick()
	{
		rlGradualGifts.setEnabled(true);
		rlGradualGifts.setAlpha(1f);
	}

	/**
	 * 不默认选择礼物--赠送礼物
	 */
	private void sendGifts()
	{
		List<Fragment> fragments = getChildFragmentManager().getFragments();
		if(fragments != null)
		{
			for(int i = 0; i < fragments.size(); i++)
			{
				GiftsFactoryFragment giftsFactoryFragment = (GiftsFactoryFragment)fragments.get(i);
				boolean chackedGift = giftsFactoryFragment.isChackedGift();
				if(chackedGift)
				{
					IMChatGiftsModel.GiftDetail checkGifts = giftsFactoryFragment.getCheckGifts();
					if(checkGifts == null)
					{
						return;
					}
					disposeSelectedGift(checkGifts);
					return;
				}
			}
		}
	}

	public void loadIMChatGiftsSuccess(ArrayList<IMChatGiftsModel> data)
	{
		setVPGifts(data);
		setRVGifts(data);
		setListener();
		mIsLoadGiftSuccess = true;
	}

	private void setVPGifts(ArrayList<IMChatGiftsModel> data)
	{
		//创建fragment的工厂类
		FragmentFactory factory = FragmentFactory.getSingleFactoryInstance();
		for(int i = 0; i < data.size(); i++)
		{
			IMChatGiftsModel imChatGiftsModel = data.get(i);
			ArrayList<IMChatGiftsModel.GiftDetail> items = imChatGiftsModel.items;
			if(items != null && items.size() > 0)
			{
				GiftsFactoryFragment giftsFactoryFragment = (GiftsFactoryFragment)factory.getGiftsFragment(items);
				giftsFragments.add(giftsFactoryFragment);
			}
		}

		//        for (int i = 0; i < data.size(); i++) {
		//            IMChatGiftsModel imChatGiftsModel = data.get(i);
		//            ArrayList<IMChatGiftsModel.GiftDetail> items = imChatGiftsModel.items;
		//            GiftsFactoryFragment giftsFactoryFragment = (GiftsFactoryFragment) factory.getGiftsFragment(items);
		//            giftsFragments.add(giftsFactoryFragment);
		//        }

		//        //礼物1
		//        GiftsFactoryFragment giftsFactoryFragment = (GiftsFactoryFragment) factory.getGiftsFragment(data);
		//        giftsFragments.add(giftsFactoryFragment);
		//
		//        //礼物2
		//        GiftsFactoryFragment giftsFactoryFragment2 = (GiftsFactoryFragment) factory.getGiftsFragment(data);
		//        giftsFragments.add(giftsFactoryFragment2);

		EmotionGiftsFragmentAdapter adapter = new EmotionGiftsFragmentAdapter(getChildFragmentManager(), giftsFragments);
		vpGifts.setAdapter(adapter);
		// TODO: 2018/8/28 缓存礼物，默认不选择礼物，全局设定选择一个礼物
		vpGifts.setOffscreenPageLimit(giftsFragments.size());
	}

	private void setRVGifts(ArrayList<IMChatGiftsModel> data)
	{
		giftsCheckedList = new ArrayList<>();
		for(int i = 0; i < data.size(); i++)
		{
			ArrayList<IMChatGiftsModel.GiftDetail> items = data.get(i).items;
			if(items != null && items.size() > 0)
			{
				EmotionGiftsCheckedModel model = new EmotionGiftsCheckedModel();
				model.icon = getResources().getDrawable(R.drawable.icon_emotion_button);
				model.flag = "礼物" + data.get(i).id;
				if(i == 0)
				{
					model.isSelected = true;
				}
				else
				{
					model.isSelected = false;
				}
				model.isGift = true;
				model.imgId = data.get(i).iconId;
				giftsCheckedList.add(model);
			}
		}

		//        for (int i = 0; i < data.size(); i++) {
		//            EmotionGiftsCheckedModel model = new EmotionGiftsCheckedModel();
		//            model.icon = getResources().getDrawable(R.drawable.icon_emotion_button);
		//            model.flag = "礼物" + data.get(i).id;
		//            model.isSelected = true;
		//            model.isGift = true;
		//            model.imgId = data.get(i).icon;
		//            giftsCheckedList.add(model);
		//        }

		//底部tab
		rvGiftsAdapter = new HorizontalRVAdapter(getActivity(), giftsCheckedList);
		rvGifts.setHasFixedSize(true);//使RecyclerView保持固定的大小,这样会提高RecyclerView的性能
		rvGifts.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false));
		rvGifts.setAdapter(rvGiftsAdapter);
		rvGiftsAdapter.setRecyclerView(rvGifts);
//        rvGifts.setItemAnimator(null);
	}

	private void setListener()
	{
		//初始化recyclerview_horizontal监听器
		rvGiftsAdapter.setOnClickItemListener(new HorizontalRVAdapter.OnClickItemListener()
		{
			@Override
			public void onItemClick(View view, int position, List<EmotionGiftsCheckedModel> datas)
			{
				if(CurrentPosition == position) return;
				//获取先前被点击tab,修改背景颜色的标记
				datas.get(CurrentPosition).isSelected = false;
				//获取当前被点击tab,修改背景颜色的标记
				datas.get(position).isSelected = true;
				//通知更新，这里我们选择性更新就行了
//                rvGiftsAdapter.notifyItemChanged(position);
//                rvGiftsAdapter.notifyItemChanged(CurrentPosition);
				rvGiftsAdapter.updateLocalData(CurrentPosition, position);
				//记录当前被选中tab下标
				CurrentPosition = position;
				//viewpager界面切换
				vpGifts.setCurrentItem(position, false);
			}

			@Override
			public void onItemLongClick(View view, int position, List<EmotionGiftsCheckedModel> datas)
			{
			}
		});

		vpGifts.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
		{
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
			{

			}

			@Override
			public void onPageSelected(int position)
			{
				if(CurrentPosition == position) return;
				//获取先前被点击tab,修改背景颜色的标记
				giftsCheckedList.get(CurrentPosition).isSelected = false;
				//获取当前被点击tab,修改背景颜色的标记
				giftsCheckedList.get(position).isSelected = true;
				//通知更新，这里我们选择性更新就行了
//                rvGiftsAdapter.notifyItemChanged(position);
//                rvGiftsAdapter.notifyItemChanged(CurrentPosition);
				rvGiftsAdapter.updateLocalData(CurrentPosition, position);
				//记录当前被选中tab下标
				CurrentPosition = position;
			}

			@Override
			public void onPageScrollStateChanged(int state)
			{

			}
		});
	}

	/**
	 * 请求网络获取礼物信息
	 */
	public abstract void requestGistsInfo();

	/**
	 * 处理选中的礼物
	 *
	 * @param checkGifts
	 */
	public abstract void disposeSelectedGift(IMChatGiftsModel.GiftDetail checkGifts);
}

package com.cstor.tanjiance.activity;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CustomMapStyleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewOptions;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.enums.PathPlanningStrategy;
import com.amap.api.navi.model.AMapCalcRouteResult;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapModelCross;
import com.amap.api.navi.model.AMapNaviCameraInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapTrafficStatus;
import com.amap.api.navi.model.NaviInfo;

import com.amap.api.navi.view.AMapModeCrossOverlay;
import com.amap.api.navi.view.TrafficProgressBar;
import com.cstor.tanjiance.R;
import com.cstor.tanjiance.bean.LatLngBean;
import com.cstor.tanjiance.bean.LatLngListBean;
import com.cstor.tanjiance.http.AppHttpUtils;
import com.cstor.tanjiance.http.HttpRequestCallback;
import com.cstor.tanjiance.listener.TaskListener;
import com.cstor.tanjiance.utils.DensityUtils;
import com.cstor.tanjiance.utils.LogUtils;
import com.cstor.tanjiance.utils.NaviUtil;
import com.cstor.tanjiance.utils.WebSocketUtil;
import com.cstor.tanjiance.view.AMapCameraOverlay;
import com.cstor.tanjiance.view.CarOverlay;
import com.cstor.tanjiance.view.DriveWayLinear;
import com.cstor.tanjiance.view.NextTurnTipView;

import com.cstor.tanjiance.view.ZoomInIntersectionView;

import org.java_websocket.enums.ReadyState;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class NavigationActivity extends BaseCustomActivity implements TaskListener {

    private TrafficProgressBar mTrafficBarView;
    private DriveWayLinear mDriveWayView;
    // 自定义实景放大图
    private ZoomInIntersectionView mRealCrossView;
    // 自定义模型放大图
    private AMapModeCrossOverlay mModeCrossView;
    private AMapCameraOverlay cameraOverlay;

    private TextView textNextRoadDistance;
    private TextView textNextRoadName;
    private NextTurnTipView nextTurnTipView;
    protected final List<LatLng> eList = new ArrayList<LatLng>();
    protected LatLng mLatlng = new LatLng(32.04137952432501,118.77951592967614);
    protected LatLng mLatlng2 = new LatLng(32.03723267802945,118.81669510816104);
    protected LatLng mLatlng3 = new LatLng(32.022172016507625,118.80490627580903);
    protected LatLng mLatlng4 = new LatLng(32.03119111934845,118.74725356333401);
    private int value = 50;
    private int co2;
    private int ch4;
    private int c2h4;
    private int nh3;
    private int c2s;
    private int h2o;
    private int c2h6;
    private long  time = 0;
    private String TAG = "NavigationActivity";
    private Context mContext;
    private LatLngBean latLngBean = new LatLngBean();
    private LatLngListBean latLngListBean = new LatLngListBean();
    private Timer timer;
    private AMap aMap;
    private MarkerOptions markerOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        mContext = this;
        mAMapNaviView = (TextureMapView) findViewById(R.id.navi_view);
        mAMapNaviView.onCreate(savedInstanceState);


//        AMapNaviViewOptions aMapNaviViewOptions = new AMapNaviViewOptions();
//        aMapNaviViewOptions.setNaviArrowVisible(true);
////        aMapNaviViewOptions.setCustomMapStylePath("/assets/style.data");
//        aMapNaviViewOptions.setAutoLockCar(true);
////        AMapNaviView aMapNaviView = new AMapNaviView(this);
//        mAMapNaviView.setViewOptions(aMapNaviViewOptions);

        aMap = mAMapNaviView.getMap();
        //该方法在AMap类中提供
        aMap.setCustomMapStyle(
                new CustomMapStyleOptions()
                        .setEnable(true)
//                        .setStyleId("de95ba87fafb348becb2a318aa68ca09")//官网控制台-自定义样式 获取
//                        .setStyleDataPath("/assets/style.data")
//                        .setStyleExtraPath("/assets/style_extra.data")
//                        .setStyleTexturePath("/mnt/sdcard/amap/textures.zip")
        );
//        aMap.setMapType(AMap.MAP_TYPE_NIGHT);

        mTrafficBarView = (TrafficProgressBar) findViewById(R.id.myTrafficBar);
        mDriveWayView = (DriveWayLinear) findViewById(R.id.myDriveWayView);
        mRealCrossView = (ZoomInIntersectionView) findViewById(R.id.myZoomInIntersectionView);

        textNextRoadDistance = (TextView) findViewById(R.id.text_next_road_distance);
        textNextRoadName = (TextView) findViewById(R.id.text_next_road_name);
        nextTurnTipView = (NextTurnTipView) findViewById(R.id.icon_next_turn_tip);

//        // 构建车辆信息
//        AMapCarInfo carInfo = new AMapCarInfo();
//        carInfo.setCarNumber("京C123456");   //设置车牌号
//        carInfo.setCarType("1");             //设置车辆类型,0:小车; 1:货车. 默认0(小车).
//        carInfo.setVehicleAxis("6");         //设置货车的轴数，mCarType = 1时候生效，取值[0-255]，默认为2
//        carInfo.setVehicleHeight("3.56");    //设置货车的高度，单位：米，mCarType = 1时候生效，取值[0-25.5],默认1.6米
//        carInfo.setVehicleLength("7.3");     //设置货车的最大长度，单位：米，mCarType = 1时候生效，取值[0-25]，默认6米
//        carInfo.setVehicleWidth("2.5");      //设置货车的最大宽度，单位：米，mCarType = 1时候生效，取值[0-25.5]，默认2.5米
//        carInfo.setVehicleSize("4");         //设置货车的大小，1-微型货车 2-轻型/小型货车 3-中型货车 4-重型货车，默认为2
//        carInfo.setVehicleLoad("25.99");     //设置货车的总重，即车重+核定载重，单位：吨，mCarType = 1时候生效，取值[0-6553.5]
//        carInfo.setVehicleWeight("20");      //设置货车的核定载重，单位：吨，mCarType = 1时候生效，取值[0-6553.5]
//        carInfo.setRestriction(true);        //设置是否躲避车辆限行，true代表躲避车辆限行，false代表不躲避车辆限行,默认为true
//        carInfo.setVehicleLoadSwitch(true);   //设置货车重量是否参与算路，true-重量会参与算路；false-重量不会参与算路。默认为false
//        AMapNavi mAMapNavi = AMapNavi.getInstance(this);
//        // 设置车辆信息
//        mAMapNavi.setCarInfo(carInfo);
//        // 算路
//        // 起点信息
//        NaviPoi start = new NaviPoi("龙城花园", new LatLng(39.993308,116.473199), "");
//        // 设置起点角度
//        start.setDirection(35);
//        // 途经点信息
//        List<NaviPoi> waysPoiIds = new ArrayList<NaviPoi>();
//        waysPoiIds.add(new NaviPoi("途经点1", null, "B000A805M7"));
//        waysPoiIds.add(new NaviPoi("途经点2", null, "B0FFFAADBU"));
//        waysPoiIds.add(new NaviPoi("途经点3", null, "B0FFF5BER7"));
//        // 终点信息
//        NaviPoi end = new NaviPoi("北京大学", new LatLng(39.917834, 116.397036), "");
//        mAMapNavi.calculateDriveRoute(start, end, waysPoiIds, PathPlanningStrategy.DRIVING_MULTIPLE_ROUTES_DEFAULT);

        cameraOverlay = new AMapCameraOverlay(this);
        mModeCrossView = new AMapModeCrossOverlay(this, aMap);
        mModeCrossView.setCrossOverlayLocation(new Rect(0, DensityUtils.dp2px(getApplicationContext(),50),
                DensityUtils.getScreenWidth(getApplicationContext()), DensityUtils.dp2px(getApplicationContext(), 300)));

        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
            @Override
            public void onMapLoaded() {
                aMap.setPointToCenter(mAMapNaviView.getWidth()/2, mAMapNaviView.getHeight()/2);

                aMap.moveCamera(CameraUpdateFactory.zoomTo(16f));
                carOverlay = new CarOverlay(NavigationActivity.this, mAMapNaviView);
            }
        });
        eList.add(mLatlng);
        eList.add(mLatlng2);
        eList.add(mLatlng3);
        eList.add(mLatlng4);
        addMarker();

        // 初始化Websocket
        WebSocketUtil.inistance(mContext).connect();
        // 写一个Timer，来保证ws保持连接
        keepWebSocketConnect();
    }

    /**
     * 添加工厂位置
     */

    public void addMarker(){
        for (int i =0; i< eList.size(); i++){
            markerOption = new MarkerOptions();
            markerOption.position(eList.get(i));
            markerOption.draggable(true);//设置Marker可拖动
            markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                    .decodeResource(getResources(),R.drawable.markericon)));
            markerOption.anchor(0.5f,0.5f);
            // 将Marker设置为贴地显示，可以双指下拉地图查看效果
            markerOption.setFlat(false);//设置marker平贴地图效果
            aMap.addMarker(markerOption);
        }

    }

    public void keepWebSocketConnect(){
        if (timer == null) {
            timer = new Timer();
        }
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if( ! WebSocketUtil.inistance(mContext).getReadyState().equals(ReadyState.OPEN)){
                    LogUtils.d( "守护进程判定ws已关闭，尝试重连");
                    WebSocketUtil.inistance(mContext).reconnect();
                }
            }
        }, 0, 10 * 1000);
    }

    /** ------- 导航基本信息的回调 ----- */
    @Override
    public void onNaviInfoUpdate(NaviInfo naviInfo) {
        super.onNaviInfoUpdate(naviInfo);
        int allLength = mAMapNavi.getNaviPath().getAllLength();

        /**
         * 导航路况条 更新
         */
        List<AMapTrafficStatus> trafficStatuses = mAMapNavi.getTrafficStatuses(0, 0);
        mTrafficBarView.update(allLength, naviInfo.getPathRetainDistance(), trafficStatuses);

        /**
         * 更新路口转向图标
         */
        nextTurnTipView.setIconType(naviInfo.getIconType());

        /**
         * 更新下一路口 路名及 距离
         */
        textNextRoadName.setText(naviInfo.getNextRoadName());
        textNextRoadDistance.setText(NaviUtil.formatKM(naviInfo.getCurStepRetainDistance()));

        /**
         * 绘制转弯的箭头
         */
        drawArrow(naviInfo);

    }

    /**
     * GPS 定位信息的回调函数
     * @param location 当前定位的GPS信息
     */
    @Override
    public void onLocationChange(AMapNaviLocation location) {

        if (carOverlay != null && location != null) {
//
            carOverlay.draw(mAMapNaviView.getMap(), new LatLng(location.getCoord().getLatitude(),
                    location.getCoord().getLongitude()), location.getBearing());
            if ((location.getTime()-time) > 1500) {
                LogUtils.d("定位时间：" + location.getTime());
                time = location.getTime();
                final List<Float> list = new ArrayList<Float>();
                for (int i = 0; i < eList.size(); i++) {
//                    LogUtils.d("计算直线距离:" + AMapUtils.calculateLineDistance(eList.get(i),
//                            new LatLng(location.getCoord().getLatitude(), location.getCoord().getLongitude())));
                    //计算直线距离
                    list.add(AMapUtils.calculateLineDistance(eList.get(i), new LatLng(location.getCoord().getLatitude(),
                            location.getCoord().getLongitude())));
                }
                float first = list.get(0);
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i) <= first) {

                        first = list.get(i);
                    }
                }
                if (first <= 1000) {
                    value = (int) (130 - (first / 1000 * 130));
                    //随机数
                    co2 = (int) (Math.random() * 150);
                    ch4 = (int) (Math.random() * 150);
                    c2h4 = (int) (Math.random() * 150);
                    nh3 = (int) (Math.random() * 150);
                    c2s = (int) (Math.random() * 150);
                    h2o = (int) (Math.random() * 150);
                    c2h6 = (int) (Math.random() * 150);
                } else {
                    value = (int) (Math.random() * 130);
                    //随机数
                    co2 = (int) (Math.random() * 150);
                    ch4 = (int) (Math.random() * 150);
                    c2h4 = (int) (Math.random() * 150);
                    nh3 = (int) (Math.random() * 150);
                    c2s = (int) (Math.random() * 150);
                    h2o = (int) (Math.random() * 150);
                    c2h6 = (int) (Math.random() * 150);
                }
                JSONArray array1 = new JSONArray();
                try {
                    JSONObject jsonObject = new JSONObject().put("lng", location.getCoord().getLongitude())
                            .put("lat", location.getCoord().getLatitude())
                            .put("value", value)
                            .put("co2", co2)
                            .put("ch4", ch4)
                            .put("c2h4", c2h4)
                            .put("nh3", nh3)
                            .put("c2s", c2s)
                            .put("h2o", h2o)
                            .put("c2h6", c2h6);
//                System.out.println(array1.toString());
//                LogUtils.d("监测坐标：" + array1.toString());
                    WebSocketUtil.sendMsg(jsonObject.toString());
                } catch (Exception e) {
                    e.toString();
                }
            }

//            try {
//                latLngBean.setLng(location.getCoord().getLongitude());//
//                latLngBean.setLat(location.getCoord().getLatitude());//
//                latLngBean.setC2h4(c2h4);//
//                latLngBean.setC2h6(c2h6);//
//                latLngBean.setCh4(ch4);//
//                latLngBean.setCo2(co2);//
//                latLngBean.setH2o(h2o);//
//                latLngBean.setNh3(nh3);
//                latLngBean.setValue(value);//
//                latLngBean.setC2s(c2s);//
////                latLngListBean.setLatLngBean(latLngBean);
////                LogUtils.e(TAG, "生成地震数据json字符串:" + object.toString());
//            } catch (Exception e) {
//                LogUtils.e(TAG, "生成地震数据json字符串异常:" + e.toString());
//            }


//            结果：[{"color":"red","height":20},{"color":"blue","height":1010}]
        }
    }



    /** ----- start 车道信息的回调 start ------- */
    @Override
    public void showLaneInfo(AMapLaneInfo aMapLaneInfo) {
        mDriveWayView.setVisibility(View.VISIBLE);
        mDriveWayView.buildDriveWay(aMapLaneInfo);

    }

    @Override
    public void hideLaneInfo() {
        mDriveWayView.hide();
    }
    /** ----- ebd 车道信息的回调 end -------*/


    /** ----- start 路口放大图 start ------- */
    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {
        mRealCrossView.setIntersectionBitMap(aMapNaviCross);
        mRealCrossView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideCross() {
        mRealCrossView.setVisibility(View.GONE);
    }

    @Override
    public void showModeCross(AMapModelCross aMapModelCross) {
        try {
            mModeCrossView.showCrossOverlay(aMapModelCross.getPicBuf1());
            LogUtils.d("路口放大图：" + aMapModelCross.getPicBuf1().length);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void hideModeCross() {
        mModeCrossView.hideCrossOverlay();
    }
    /** ----- end 路口放大图 end ------- */

    /** ----- start 电子眼  start --------*/
    @Override
    public void updateCameraInfo(AMapNaviCameraInfo[] aMapCameraInfos) {
        if (cameraOverlay != null) {
            cameraOverlay.draw(mAMapNaviView.getMap(), aMapCameraInfos);
        }

    }
    /** ----- end 电子眼  end --------*/

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mRealCrossView != null) {
            mRealCrossView.recycleResource();
        }

        if (mModeCrossView != null) {
            mModeCrossView.hideCrossOverlay();
        }

        if (cameraOverlay != null) {
            cameraOverlay.destroy();
        }
        WebSocketUtil.closeConnect();
    }

    @Override
    public void onInitNaviSuccess() {
        super.onInitNaviSuccess();
        /**
         * 方法: int strategy=mAMapNavi.strategyConvert(congestion, avoidhightspeed, cost, hightspeed, multipleroute); 参数:
         *
         * @congestion 躲避拥堵
         * @avoidhightspeed 不走高速
         * @cost 避免收费
         * @hightspeed 高速优先
         * @multipleroute 多路径
         *
         *  说明: 以上参数都是boolean类型，其中multipleroute参数表示是否多条路线，如果为true则此策略会算出多条路线。
         *  注意: 不走高速与高速优先不能同时为true 高速优先与避免收费不能同时为true
         */
        int strategy = 0;
        try {
            //再次强调，最后一个参数为true时代表多路径，否则代表单路径
            strategy = mAMapNavi.strategyConvert(true, false, false, false, false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mAMapNavi.calculateDriveRoute(start, end, waysPoiIds, PathPlanningStrategy.DRIVING_MULTIPLE_ROUTES_DEFAULT);
        aMap.setMapType(AMap.MAP_TYPE_SATELLITE);// 设置卫星地图模式，aMap是地图控制器对象。
    }

    @Override
    public void onCalculateRouteSuccess(AMapCalcRouteResult aMapCalcRouteResult) {
        super.onCalculateRouteSuccess(aMapCalcRouteResult);
        mAMapNavi.startNavi(NaviType.EMULATOR);
    }

    @Override
    public void onEndEmulatorNavi() {
        //结束模拟导航
        //发送json
//        LogUtils.e(TAG, "生成地震数据json字符串:" + latLngBean.toString());
//        postReceiveEq();
    }


    /**
     * 发送地震预警的bean,转为json
     */

    public void postReceiveEq() {

        AppHttpUtils.postBean(mContext, latLngListBean, new HttpRequestCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtils.e("获取结果:" + result);

            }

            @Override
            public void onFailure(String error) {
                LogUtils.i(TAG, "--------失败onFailure--------" + error);
            }
        });

//
//        AppHttpUtils.postJson(mContext, Constans.faceRecognize, latLngBean, new HttpRequestCallback() {
//            @Override
//            public void onSuccess(String result) {
//                LogUtils.i(TAG, "--------地震数据json onSuccess--------" + result);
//            }
//
//            @Override
//            public void onFailure(String error) {
//                LogUtils.i(TAG, "--------地震数据json onFailure--------" + error);
//            }
//        });
    }

    @Override
    public void task_message(String TaskName, String param) {
        LogUtils.d("接收到TASK指令，TaskName:" + TaskName + ", param:" + param);

        // 这里的Taskname和服务端那边约定好类型就可以了，没有什么限制的，然后匹配到对应的操作
        switch (TaskName) {
            case TaskListener.SYSTEM_UPDATE:
//                SystemHelper.wakeUpAndUnlock(this);
//                runOnUiThread(() -> SystemHelper.update(MainActivity.this, param));
                break;
            case TaskListener.SYSTEM_REBOOT:
//                SystemHelper.wakeUpAndUnlock(this);
//                SpeechHelper.speech(this, "系统即将重启");
                break;
            case TaskListener.MEET_REFRESH:
//                SystemHelper.wakeUpAndUnlock(this);
//                SpeechHelper.speech(this, "收到下发资料");
                break;
            case TaskListener.DEBUG:
//                SystemHelper.wakeUpAndUnlock(this);
//                SpeechHelper.speech(this, "收到服务器指令");
//                new Thread(()
//                        -> runOnUiThread(()
//                        -> Toast.makeText(MainActivity.this, "交互指令：" + param, Toast.LENGTH_SHORT).show()
//                )).start();
                break;
            default:
//                SystemHelper.wakeUpAndUnlock(this);
//                SpeechHelper.speech(this, "无效命令");
                break;
        }
    }
}

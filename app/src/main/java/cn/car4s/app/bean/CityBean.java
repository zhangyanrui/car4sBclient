package cn.car4s.app.bean;

import java.util.List;

/**
 * Description:
 * Author: Alex
 * Email: xuebo.chang@langtaojin.com
 * Time: 2015/6/23.
 */
public class CityBean extends BaseBean {
//    "ProvinceID": "1",
//            "ProvinceName": "北京市",
//            "CityList": [
//    {
//        "CityID": "1",
//            "CityName": "北京市",
//            "AreaList": [
//        {
//            "AreaID": "1",
//                "AreaName": "东城区"
//        },
//        {
//            "AreaID": "2",
//                "AreaName": "西城区"
//        }
//        ]
//    }
//    ]
//},
//        {
//        "ProvinceID": "2",
//        "ProvinceName": "天津市",
//        "CityList": [
//        {
//        "CityID": "2",
//        "CityName": "天津市",
//        "AreaList": [
//        {
//        "AreaID": "19",
//        "AreaName": "和平区"
//        },
//        {
//        "AreaID": "20",
//        "AreaName": "河东区"
//        }
//        ]
//        }
//        ]
//        }
//        ]

    public String CityID;
    public String CityName;
    public List<AreaBean> AreaList;

    public static class AreaBean extends BaseBean {
        public String AreaID;
        public String AreaName;
    }

}

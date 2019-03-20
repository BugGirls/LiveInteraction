package com.hndt.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import lombok.Data;


/**
 * @author Hystar
 * @date 2018/2/2
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageVo {
    private Long id;

    private String source;

    private String file_type;

    private String channel_id;

    private String filePath;

    private String content;

    private String add_msg;

    private String wave;

    private Integer total_duration;

    private String nickname;

    private String icon;

    private String from_uid;

    private String open_id;

    private String union_id;

    private Integer sex;

    private String city;

    private String country;

    private String province;

    private Long create_time;

    private String sendTime;

    private String remark;
    /**
     * 是否自动发布
     */
    @XStreamOmitField
    private boolean checked;

    public MessageVo(Long id, String source, String file_type, String channel_id, String content, String add_msg, String wave, Integer total_duration, String nickname, String icon, String from_uid, String open_id, String union_id, Integer sex, String city, String country, String province, Long create_time, boolean checked) {
        this.id = id;
        this.source = source;
        this.file_type = file_type;
        this.channel_id = channel_id;
        this.content = content;
        this.add_msg = add_msg;
        this.wave = wave;
        this.total_duration = total_duration;
        this.nickname = nickname;
        this.icon = icon;
        this.from_uid = from_uid;
        this.open_id = open_id;
        this.union_id = union_id;
        this.sex = sex;
        this.city = city;
        this.country = country;
        this.province = province;
        this.create_time = create_time;
        this.checked = checked;
    }

    public MessageVo() {
        super();
    }
}

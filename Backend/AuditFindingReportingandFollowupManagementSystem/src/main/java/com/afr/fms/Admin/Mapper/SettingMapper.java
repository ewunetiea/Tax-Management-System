package com.afr.fms.Admin.Mapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import com.afr.fms.Admin.Entity.Setting;

@Mapper
public interface SettingMapper {
    @Select("select * from setting")
    public Setting getSetting();

    @Insert("insert into setting(maximum_attempt,lock_time,credential_expiration) values(#{maximum_attempt},#{lock_time},#{credential_expiration})")
    public void create_account_setting(Setting setting);

    @Insert("insert into setting(jwt_expiration,jwt_refresh_token_expiration,jwt_secret) values(#{jwt_expiration},#{jwt_refresh_token_expiration},#{jwt_secret})")
    public void create_jwt_setting(Setting setting);

    @Update("update setting set maximum_attempt=#{maximum_attempt}, lock_time=#{lock_time}, credential_expiration=#{credential_expiration} where id=#{id}")
    public void updateAccountSetting(Setting setting);

    @Update("update setting set jwt_expiration=#{jwt_expiration},jwt_refresh_token_expiration=#{jwt_refresh_token_expiration}, jwt_secret=#{jwt_secret} where id=#{id}")
    public void updateJWTSetting(Setting setting);
}

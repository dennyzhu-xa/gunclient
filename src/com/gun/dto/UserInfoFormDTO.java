package com.gun.dto;

public class UserInfoFormDTO {

	

	public static enum ATTRIBUTE {
	    ID("id"),
	    USER_ID("userId"),
	    USER_NAME("userName"),
	    EMAIL("email"),
	    PASSWORD("password"),
	    ROLE_ID("roleId"),
	    CREATE_BY_ID("createById"),
	    CREATED_BY_NAME("createdByName"),
	    CREATED_DATE("createdDate"),
	    UPDATED_BYID("updatedById"),
	    UPDATED_BY_NAME("updatedByName"),
	    UPDATED_DATE("updatedDate")
	    ;
	    
	    private String value;
	    ATTRIBUTE(String value) {
	      this.value = value;
	    };
	    public String getValue() {
	      return this.value;
	    }
	  };
	  
	  private String id;
	  private String userId;
	  private String userName;
	  private String email;
	  private String roleId;
	  private String roleName;
	
	  public UserInfoFormDTO() {
	  }
	
	  public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
	    return this.userId;
	  }
	
	  public void setUserId(String userId) {
	    this.userId = userId;
	  }
	  public String getUserName() {
	    return this.userName;
	  }
	
	  public void setUserName(String userName) {
	    this.userName = userName;
	  }
	  public String getEmail() {
	    return this.email;
	  }
	
	  public void setEmail(String email) {
	    this.email = email;
	  }

	public String getRoleId() {
		return roleId;
	}
	
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	
	public String getRoleName() {
		return roleName;
	}
	
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	  
}

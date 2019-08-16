package cn.com.gehj.httpmaster.Download;

public  class DownLoadConfig {


    private  int coreThreadSize;
    private int maxThreadSize;
    private int localProgressThreadSize;

    /*--------build完实例之后可以查看参数,get方法*/

    public int CoreThreadSize() {
        return coreThreadSize;
    }

    public int MaxThreadSize() {
        return maxThreadSize;
    }

    public int LocalProgressThreadSize() {
        return localProgressThreadSize;
    }
    /*--------build完实例之后可以查看参数,get方法*/


     DownLoadConfig(Builder builder) {//初始化;
            coreThreadSize = builder.coreThreadSize==0?DownLoadManger.CORE_THREAD:builder.coreThreadSize;//builder模式指定默认值;
            maxThreadSize = builder.maxThreadSize==0?DownLoadManger.MAX_THREAD:builder.maxThreadSize;
            localProgressThreadSize = builder.localProgressThreadSize==0?DownLoadManger.LOCAL_PROGRASS_SIZE:builder.localProgressThreadSize;
    }
     public static class Builder{
        private  int coreThreadSize;
        private int maxThreadSize;
        private int localProgressThreadSize;

        public Builder CoreThreadSize(int coreThreadSize) {
            this.coreThreadSize = coreThreadSize;
            return  this;
        }

        public Builder MaxThreadSize(int maxThreadSize) {
            this.maxThreadSize = maxThreadSize;
            return  this;
        }

        public Builder LocalProgressThreadSize(int localProgressThreadSize) {
            this.localProgressThreadSize = localProgressThreadSize;
            return  this;
        }

        public DownLoadConfig build(){//通过这个方法返回实例;
                return new DownLoadConfig(this);
        }
    }
}

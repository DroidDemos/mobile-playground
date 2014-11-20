#include <cn_beriru_jni_CallJniActivity.h>
#include "sstream"

using namespace std;

const char* TAG = __FILE__ ;


JNIEXPORT jstring JNICALL Java_cn_beriru_jni_CallJniActivity_concat (JNIEnv * env,jobject self,jstring str)
{
	const char* s = _s(env,str);
	string jniStr = "string from jni";
	return env->NewStringUTF(jniStr.append(s).c_str());
}



JNIEXPORT jint JNICALL Java_cn_beriru_jni_CallJniActivity_initWatchDog(JNIEnv *env, jobject obj)
{
	int child_pid = init_watchdog();
	return child_pid;
}


const char* _s(JNIEnv* env, jstring str)
{
	const char* ret = env->GetStringUTFChars(str,0);
	return ret;
}


void loge(const string& content)
{
	__android_log_write(ANDROID_LOG_ERROR, TAG, content.c_str());
}

void panic(const string& s)
{
	loge("panic : " + s);
}

int init_watchdog()
{
	string monitor_dir = "/data/data/cn.beriru.playground";

	pid_t pid = fork();

	if(pid < 0)
	{
		panic("fork error");
	}
	else if(pid == 0)
	{
		loge("in child process");
		// child_process(monitor_dir);
		// start_monitor(monitor_dir);
		// poll_monitor(monitor_dir);
		epoll_monitor(monitor_dir);
		return 0;
	}
	else
	{
		stringstream s;
		s <<  "in parent process child id is ";
		s << pid;
		loge(s.str().c_str());
		waitpid(-1,NULL,WNOHANG);
		return pid;
	}
}


void poll_monitor(const string& dir)
{
	int i = 0;
	while(true)
	{
		sleep(1);
		if(access(dir.c_str(),F_OK) != 0)
		{
			launch_browser();
			return ;
		}
		else
		{
			stringstream ss;
			ss << "retry at " << i ;
			loge(ss.str().c_str());
		}
		i++;
	}
	return ;
}



void select_monitor(const string& dir)
{
	int fd = inotify_init();
	int wd = inotify_add_watch(fd,dir.c_str(),IN_DELETE_SELF);
	while(true)
	{
		fd_set fds;
		FD_ZERO(&fds);
		FD_SET(fd,&fds);
		if(select(fd + 1,&fds,NULL,NULL,NULL) > 0)
		{
			launch_browser();
			return ;
		}
		else
		{
			loge("select event happen!");
		}
	}
}


void epoll_monitor(const string& monitor_dir)
{
	struct pollfd dog;
	// struct pollfd dog_list[] = { dog };

	dog.fd = inotify_init();
	dog.events = POLLIN;
	int watch_ret = inotify_add_watch(dog.fd,monitor_dir.c_str(),IN_DELETE | IN_DELETE_SELF | IN_CLOSE);
	if(watch_ret < 0)
	{
		panic("error watch");
		return ;
	}

	loge("enter loop");

	while(true){
		loge("another loop");
		int ret = poll(&dog, 1, -1);
		stringstream ss;
		ss << "poll result : ret " << ret << " event: " << dog.revents;
		loge(ss.str().c_str());

		if(ret < 0)
		{
			panic("poll error");
			return;
		}
		else
		{
			if((dog.revents & POLLIN) == POLLIN)
			{
				loge("pollin event!");
				if(access(monitor_dir.c_str(),F_OK) != 0)
				{
					launch_browser();
					return ;
				}
				else
				{
					loge("exeption: poll in but dir exists");
					inotify_rm_watch(dog.fd, watch_ret);
					dog.fd = inotify_init();
					dog.events = POLLIN;
					watch_ret = inotify_add_watch(dog.fd,monitor_dir.c_str(),IN_CLOSE | IN_DELETE | IN_DELETE_SELF);
				}
			}
			else
			{
				stringstream ss;
				ss << "poll ok but not POLLIN ?? ";
				ss << dog.revents;
				loge("poll o");
				sleep(1);
			}
		}
	}

	inotify_rm_watch(dog.fd,watch_ret);
	return ;
}


void launch_browser()
{
	loge(__FUNCTION__);
	sleep(2);
	string cmd = "am start -a android.intent.action.VIEW -d http://konata.github.io ";
	string cmd_with_init = "am start --user 0 -a android.intent.action.VIEW -d http://konata.github.io ";
	system(cmd_with_init.c_str());
	return ;
}




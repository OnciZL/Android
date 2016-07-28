package com.github.oncizl.demo.rx;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.util.LruCache;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.oncizl.demo.R;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.functions.Func3;
import rx.observables.GroupedObservable;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class RxJavaActivity extends AppCompatActivity implements View.OnClickListener {

	private static final String TAG = "RxJavaActivity";

	private TextView mTextCreate, mTextJust, mTextDefer, mTextFrom, mTextRang,
			mTextTimer, mTextRepeat, mTextInterval, mTextBuffer, mTextBufferTime,
			mTextWindow, mTextWindowTime, mTextSchedulePeriodically, mTextThrottleFirst,
			mTextDebounce, mTextLift, mTextMap, mTextFlatMap, mTextCast, mTextScan,
			mTextGroupBy, mTextDistinct, mTextDistinctUtilChanged, mTextCompose, mTextDoOnSubscribe,
			mTextMerge, mTextConcatFirst;
	private ImageView mImageSchedulers, mImageMultiObserveOn, mImageConcatFirst;
	private EditText mEditCombineLatest0, mEditCombineLatest1, mEditCombineLatest2;

	private CompositeSubscription mCompositeSubscription = new CompositeSubscription();
	private LruCache<String, Bitmap> mMemoryLruCache = new LruCache<>(16);


	private void immersedStatusBar() {
		Window window = getWindow();
		if (Build.VERSION.SDK_INT >= 19) {
			window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
			if (Build.VERSION.SDK_INT >= 21) {
				window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//				window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
				window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
				window.setStatusBarColor(Color.TRANSPARENT);
			}
		}

		Resources res = getResources();
		int statusBarHeight = res.getDimensionPixelSize(res.getIdentifier("status_bar_height", "dimen", "android"));
		View statusBarPlaceholder = findViewById(R.id.status_bar_place_holder);
		if (statusBarPlaceholder != null) statusBarPlaceholder.getLayoutParams().height = statusBarHeight;

		int navigationBarHeight = res.getDimensionPixelSize(res.getIdentifier("navigation_bar_height", "dimen", "android"));
		View navigationBarPlaceholder = findViewById(R.id.navigation_bar_place_holder);
		if (navigationBarPlaceholder != null) navigationBarPlaceholder.getLayoutParams().height = navigationBarHeight;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rx_java);

		immersedStatusBar();

		mTextCreate = (TextView) findViewById(R.id.text_create);
		mTextJust = (TextView) findViewById(R.id.text_just);
		mTextDefer = (TextView) findViewById(R.id.text_defer);
		mTextFrom = (TextView) findViewById(R.id.text_from);
		mTextRang = (TextView) findViewById(R.id.text_rang);
		mTextTimer = (TextView) findViewById(R.id.text_timer);
		mTextRepeat = (TextView) findViewById(R.id.text_repeat);
		mTextInterval = (TextView) findViewById(R.id.text_interval);
		mTextBuffer = (TextView) findViewById(R.id.text_buffer);
		mTextBufferTime = (TextView) findViewById(R.id.text_buffer_time);
		mTextWindow = (TextView) findViewById(R.id.text_window);
		mTextWindowTime = (TextView) findViewById(R.id.text_window_time);
		mTextSchedulePeriodically = (TextView) findViewById(R.id.text_schedule_periodically);
		mTextThrottleFirst = (TextView) findViewById(R.id.text_throttleFirst);
		mTextDebounce = (TextView) findViewById(R.id.text_debounce);
		mTextLift = (TextView) findViewById(R.id.text_lift);
		mTextMap = (TextView) findViewById(R.id.text_map);
		mTextFlatMap = (TextView) findViewById(R.id.text_flat_map);
		mTextCast = (TextView) findViewById(R.id.text_cast);
		mTextScan = (TextView) findViewById(R.id.text_scan);
		mTextGroupBy = (TextView) findViewById(R.id.text_group_by);
		mTextDistinct = (TextView) findViewById(R.id.text_distinct);
		mTextDistinctUtilChanged = (TextView) findViewById(R.id.text_distinct_util_changed);
		mTextCompose = (TextView) findViewById(R.id.text_compose);
		mTextDoOnSubscribe = (TextView) findViewById(R.id.text_do_on_subscribe);
		mTextMerge = (TextView) findViewById(R.id.text_merge);
		mTextConcatFirst = (TextView) findViewById(R.id.text_concat_first);

		mImageSchedulers = (ImageView) findViewById(R.id.image_schedulers);
		mImageMultiObserveOn = (ImageView) findViewById(R.id.image_multi_observeOn);
		mImageConcatFirst = (ImageView) findViewById(R.id.image_concat_first);

		mEditCombineLatest0 = (EditText) findViewById(R.id.edit_combine_latest_0);
		mEditCombineLatest1 = (EditText) findViewById(R.id.edit_combine_latest_1);
		mEditCombineLatest2 = (EditText) findViewById(R.id.edit_combine_latest_2);


		// throttleFirst（防抖动），点击立即生效，之后在指定时间内，任意点击次数都不会生效，时间过后，点击又立即生效
		mCompositeSubscription.add(RxView.clicks(findViewById(R.id.btn_throttleFirst))
				.throttleFirst(2, TimeUnit.SECONDS)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Action1<Void>() {
					@Override
					public void call(Void vd) {
						mTextThrottleFirst.append("throttleFirst" + "  ");
						if (mTextThrottleFirst.getLineCount() > 3) mTextThrottleFirst.setText("throttleFirst" + "  ");
					}
				}));
		// debounce，点击后间隔时间内触发，事件没触发之前，下一次点击会取消上一次的点击，如果持续点击，事件会一直往后推迟触发
		mCompositeSubscription.add(RxView.clicks(findViewById(R.id.btn_debounce))
				.debounce(2, TimeUnit.SECONDS)
				.observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Void>() {
					@Override
					public void call(Void vd) {
						mTextDebounce.append("debounce" + "  ");
						if (mTextDebounce.getLineCount() > 3) mTextDebounce.setText("debounce" + "  ");
					}
				}));
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mCompositeSubscription.unsubscribe();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.btn_create:
				create();
				break;
			case R.id.btn_just:
				just();
				break;
			case R.id.btn_defer:
				defer();
				break;
			case R.id.btn_from:
				from();
				break;
			case R.id.btn_rang:
				rang();
				break;
			case R.id.btn_timer:
				timer(view);
				break;
			case R.id.btn_repeat:
				repeat();
				break;
			case R.id.btn_interval:
				interval(view);
				break;
			case R.id.btn_buffer:
				buffer();
				break;
			case R.id.btn_buffer_time:
				bufferTime(view);
				break;
			case R.id.btn_window:
				window();
				break;
			case R.id.btn_window_time:
				windowTime(view);
				break;
			case R.id.btn_schedule_periodically:
				schedulePeriodically(view);
				break;
			case R.id.btn_schedulers:
				schedulers();
				break;
			case R.id.btn_lift:
				lift();
				break;
			case R.id.btn_map:
				map();
				break;
			case R.id.btn_flat_map:
				flatMap();
				break;
			case R.id.btn_cast:
				cast();
				break;
			case R.id.btn_scan:
				scan();
				break;
			case R.id.btn_group_by:
				groupBy();
				break;
			case R.id.btn_distinct:
				distinct();
				break;
			case R.id.btn_distinct_util_changed:
				distinctUtilChanged();
				break;
			case R.id.btn_multi_observeOn:
				multiObserveOn();
				break;
			case R.id.btn_compose:
				compose();
				break;
			case R.id.btn_do_on_subscribe:
				doOnSubscribe();
				break;
			case R.id.btn_combine_latest:
				combineLatest(view);
				break;
			case R.id.btn_merge:
				merge();
				break;
			case R.id.btn_concat_first:
				concatFirst();
				break;
		}
	}

	private void concatFirst() {// 三级缓存
		final String imgUrl = "http://img4.5sing.kgimg.com/force/T1shYvBXYT1RXrhCrK_100_100.jpg";
		String filename = "threeCache";
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
			md.update(imgUrl.getBytes("utf-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (md != null) {
			filename = new BigInteger(1, md.digest()).toString(16);
			int offset = 32 - filename.length();
			if (offset > 0) {
				for (int i = 0; i < offset; i++) {
					filename = "0" + filename;
				}
			}
		}
		final File file = new File(getCacheDir() + File.separator + filename);
		new AlertDialog.Builder(this)
				.setItems(new String[]{"显示", "清除内存", "清除内存和磁盘"}, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (which == 0) {
							RxTextView.text(mTextConcatFirst).call("");
							final long start = System.currentTimeMillis();
							Observable<ThreeCache> memoryObservable = Observable.just(imgUrl)// 内存
									.observeOn(Schedulers.newThread())
									.map(new Func1<String, ThreeCache>() {
										@Override
										public ThreeCache call(String string) {
											ThreeCache threeCache = new ThreeCache(string);
											Bitmap bitmap = mMemoryLruCache.get(string);
											threeCache.bitmap(bitmap).from("内存");
											return threeCache;
										}
									});
							Observable<ThreeCache> diskObservable = Observable.just(imgUrl)// 磁盘
									.observeOn(Schedulers.io())
									.map(new Func1<String, ThreeCache>() {
										@Override
										public ThreeCache call(String string) {
											ThreeCache threeCache = new ThreeCache(string);
											Bitmap bitmap = null;
											if (file.exists() && file.length() > 0) {
												bitmap = BitmapFactory.decodeFile(file.getPath());
												mMemoryLruCache.put(threeCache.url(), bitmap);
											}
											threeCache.bitmap(bitmap).from("磁盘");
											return threeCache;
										}
									});
							Observable<ThreeCache> networkObservable = Observable.just(imgUrl)// 网络
									.observeOn(Schedulers.io())
									.map(new Func1<String, ThreeCache>() {
										@Override
										public ThreeCache call(String string) {
											ThreeCache threeCache = new ThreeCache(string);
											try {
												URL url = new URL(string);
												HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

												byte[] bytes = new byte[1024];
												ByteArrayOutputStream baos = new ByteArrayOutputStream();
												int len = 0;
												InputStream is = httpURLConnection.getInputStream();
												while (-1 != (len = is.read(bytes))) {
													baos.write(bytes, 0, len);
												}
												byte[] bitBytes = baos.toByteArray();
												baos.flush();
												baos.close();
												is.close();
												httpURLConnection.disconnect();
												Bitmap bitmap = BitmapFactory.decodeByteArray(bitBytes, 0, bitBytes.length);
												threeCache.bitmap(bitmap).from("网络");

												mMemoryLruCache.put(threeCache.url(), threeCache.bitmap());
												if (!file.exists()) file.createNewFile();
												FileOutputStream fos = new FileOutputStream(file);
												fos.write(bitBytes);
												fos.flush();
												fos.close();
											} catch (Exception e) {
												e.printStackTrace();
											}
											return threeCache;
										}
									});
							mCompositeSubscription.add(Observable.concat(memoryObservable, diskObservable, networkObservable)
									.first(new Func1<ThreeCache, Boolean>() {
										@Override
										public Boolean call(ThreeCache threeCache) {
											return threeCache.bitmap() != null;
										}
									})
									.observeOn(AndroidSchedulers.mainThread())
									.subscribe(new Action1<ThreeCache>() {
										@Override
										public void call(ThreeCache threeCache) {
											long end = System.currentTimeMillis();
											RxTextView.text(mTextConcatFirst).call(threeCache.from() + " - " + (end - start) + "ms");
											if (threeCache.bitmap() != null) {
												mImageConcatFirst.setImageBitmap(threeCache.bitmap());
											}
										}
									}));
						} else if (which == 1) {
							mMemoryLruCache.remove(imgUrl);
							RxTextView.text(mTextConcatFirst).call("");
							mImageConcatFirst.setImageBitmap(null);
						} else if (which == 2) {
							mMemoryLruCache.remove(imgUrl);
							if (file.exists()) file.delete();
							RxTextView.text(mTextConcatFirst).call("");
							mImageConcatFirst.setImageBitmap(null);
						}
					}
				})
				.create()
				.show();
	}

	private void merge() {// 合并事件源
		RxTextView.text(mTextMerge).call("");
		Observable<String[]> observable0 = Observable.just("item_0_")
				.map(new Func1<String, String[]>() {
					@Override
					public String[] call(String string) {
						int count = (int) (Math.random() * 5 + 1);
						String[] strings = new String[count];
						for (int i = 0; i < count; i++) {
							strings[i] = string + i;
						}
						return strings;
					}
				});
		Observable<String[]> observable1 = Observable.just("item_1_")
				.map(new Func1<String, String[]>() {
					@Override
					public String[] call(String string) {
						int count = (int) (Math.random() * 5 + 1);
						String[] strings = new String[count];
						for (int i = 0; i < count; i++) {
							strings[i] = string + i;
						}
						return strings;
					}
				});
		mCompositeSubscription.add(Observable.merge(observable0, observable1)
				.subscribeOn(Schedulers.newThread())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Action1<String[]>() {
					@Override
					public void call(String[] strings) {
						for (String string : strings) {
							mTextMerge.append(string + "\n");
						}
					}
				}));
	}

	private void combineLatest(final View view) {// 联合判断
		RxView.enabled(view).call(false);
		RxTextView.text(mEditCombineLatest0).call("");
		RxTextView.text(mEditCombineLatest1).call("");
		RxTextView.text(mEditCombineLatest2).call("");

		Observable<CharSequence> observable0 = RxTextView.textChanges(mEditCombineLatest0).skip(1);
		Observable<CharSequence> observable1 = RxTextView.textChanges(mEditCombineLatest1).skip(1);
		Observable<CharSequence> observable2 = RxTextView.textChanges(mEditCombineLatest2).skip(1);

		mCompositeSubscription.add(Observable
				.combineLatest(observable0, observable1, observable2, new Func3<CharSequence, CharSequence, CharSequence, Boolean>() {
					@Override
					public Boolean call(CharSequence charSequence0, CharSequence charSequence1, CharSequence charSequence2) {
						return (!TextUtils.isEmpty(charSequence0) && charSequence0.toString().equals("abc"))
								&& (!TextUtils.isEmpty(charSequence1) && TextUtils.isDigitsOnly(charSequence1) && Integer.parseInt(String.valueOf(charSequence1)) > 10)
								&& (!TextUtils.isEmpty(charSequence2) && charSequence2.length() > 3);
					}
				})
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Action1<Boolean>() {
					@Override
					public void call(Boolean bool) {
						RxView.enabled(view).call(bool);
					}
				}));
	}

	private void doOnSubscribe() {// 耗时加载示例
		RxTextView.text(mTextDoOnSubscribe).call("");
		final ProgressDialog progressDialog = new ProgressDialog(this);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setMessage("加载效果演示...");
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		mCompositeSubscription.add(Observable.just("result")
				.subscribeOn(Schedulers.newThread())
				.doOnSubscribe(new Action0() {// 受离他最近的下面的 subscribeOn的mainThread 线程影响
					@Override
					public void call() {
						progressDialog.show();
						mTextDoOnSubscribe.append("show");
					}
				})
				.subscribeOn(AndroidSchedulers.mainThread())
				.delay(2, TimeUnit.SECONDS)// 这个会影响他后面的操作的线程
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<String>() {
					@Override
					public void onCompleted() {
						mTextDoOnSubscribe.append("dismiss");
						progressDialog.dismiss();
					}

					@Override
					public void onError(Throwable e) {
						mTextDoOnSubscribe.append("  " + "error");
						progressDialog.dismiss();
					}

					@Override
					public void onNext(String string) {
						mTextDoOnSubscribe.append("  " + string + "  ");
					}
				}));
	}

	private void compose() {// 相对lift来说，是针对 Observable 自身进行变换
		RxTextView.text(mTextCompose).call("");
		mCompositeSubscription.add(Observable.just(1, 2, 3, 4, 5, 6)
				.compose(new Observable.Transformer<Integer, String>() {
					@Override
					public Observable<String> call(Observable<Integer> integerObservable) {
						return integerObservable.map(new Func1<Integer, String>() {
							@Override
							public String call(Integer integer) {
								return String.valueOf(integer) + "  ";
							}
						});
					}
				})
				.subscribe(new Action1<String>() {
					@Override
					public void call(String string) {
						mTextCompose.append(string + "  ");
					}
				}));
	}

	private void multiObserveOn() {// 可以通过 observeOn 多次切换线程
		mCompositeSubscription.add(Observable.just(new Character[]{'i', 'm', 'a', 'g', 'e', '.', 'p', 'n', 'g'})// 受 subscribeOn的newThread 线程影响
				.subscribeOn(Schedulers.newThread())// 只有第一个有效
				.observeOn(Schedulers.newThread())
				.map(new Func1<Character[], String>() {// 受上面的 observeOn的newThread 线程影响

					StringBuilder builder = new StringBuilder();

					@Override
					public String call(Character[] characters) {
						for (Character character : characters) {
							builder.append(character);
						}
						return builder.toString();
					}
				})
				.observeOn(Schedulers.io())
				.map(new Func1<String, InputStream>() {// 受上面的 observeOn的io 线程影响
					@Override
					public InputStream call(String name) {
						try {
							return getAssets().open(name);
						} catch (IOException e) {
							e.printStackTrace();
						}
						return null;
					}
				})
				.observeOn(Schedulers.newThread())
				.map(new Func1<InputStream, Bitmap>() {// 受上面的 observeOn的newThread 线程影响
					@Override
					public Bitmap call(InputStream inputStream) {
						return BitmapFactory.decodeStream(inputStream);
					}
				})
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Action1<Bitmap>() {// 受上面的 observeOn的mainThread 线程影响
					@Override
					public void call(Bitmap bitmap) {
						if (bitmap != null) {
							mImageMultiObserveOn.setImageBitmap(bitmap);
						}
					}
				}));
	}

	private void groupBy() {// 对数据进行按自定义规则分组，在处理和发射数据的时候，线程之间的切换可能会导致分组错乱
		RxTextView.text(mTextGroupBy).call("");
		List<Integer> list = new ArrayList<>();
		for (int i = 0; i < 50; i++) {
			int value;
			do {
				value = (int) (Math.random() * 100 + 1);
			} while (list.contains(value));
			list.add(value);
		}
		mCompositeSubscription.add(Observable.from(list)
				.observeOn(Schedulers.newThread())
				.groupBy(new Func1<Integer, String>() {
					@Override
					public String call(Integer integer) {
						String string = (integer & 1) != 0 ? "奇" : "偶";
						if (integer > 50) {
							string += "（>50）：";
						} else {
							string += "（<=50）：";
						}
						return string;
					}
				}, new Func1<Integer, String>() {
					@Override
					public String call(Integer integer) {
						return String.valueOf(integer.intValue());
					}
				})
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Action1<GroupedObservable<String, String>>() {
					@Override
					public void call(final GroupedObservable<String, String> groupedObservable) {
						if (!TextUtils.isEmpty(mTextGroupBy.getText().toString())) mTextGroupBy.append("\n");
						mTextGroupBy.append(groupedObservable.getKey());
						mCompositeSubscription.add(groupedObservable
//								.observeOn(AndroidSchedulers.mainThread())
								.subscribe(new Action1<String>() {
									@Override
									public void call(String string) {
										mTextGroupBy.append(string + "  ");
									}
								}));
					}
				}));
	}

	private void distinct() {// 去除重复的
		RxTextView.text(mTextDistinct).call("");
		Integer[] ints = new Integer[]{1, 2, 3, 4, 5, 4, 3, 2, 1, 6, 7, 6, 9, 8};
		for (int i : ints) {
			mTextDistinct.append(String.valueOf(i) + "  ");
		}
		mTextDistinct.append("\n");
		mCompositeSubscription.add(Observable.from(ints)
				.distinct()
				.subscribe(new Action1<Integer>() {
					@Override
					public void call(Integer integer) {
						mTextDistinct.append(String.valueOf(integer.intValue()) + "  ");
					}
				}));
	}

	private void distinctUtilChanged() {// 去除连续重复的，不连续的重复不会去除
		RxTextView.text(mTextDistinctUtilChanged).call("");
		Integer[] ints = new Integer[]{2, 2, 3, 3, 3, 4, 3, 2, 2, 4, 7, 6, 5, 5};
		for (int i : ints) {
			mTextDistinctUtilChanged.append(String.valueOf(i) + "  ");
		}
		mTextDistinctUtilChanged.append("\n");
		mCompositeSubscription.add(Observable.from(ints)
				.distinctUntilChanged()
				.subscribe(new Action1<Integer>() {
					@Override
					public void call(Integer integer) {
						mTextDistinctUtilChanged.append(String.valueOf(integer.intValue()) + "  ");
					}
				}));
	}

	private void scan() {// 将上两个数据的处理结果和下一个数据再次进行处理并发射，有点类似递归
		RxTextView.text(mTextScan).call("");
		mCompositeSubscription.add(Observable.just(2, 2, 3, 4, 5, 6, 7, 8, 9)
				.scan(new Func2<Integer, Integer, Integer>() {
					@Override
					public Integer call(Integer integer1/*上一次处理过后的返回结果*/, Integer integer2/*定义好的数据源*/) {
						return integer1 + integer2;
					}
				})
				.subscribe(new Action1<Integer>() {
					@Override
					public void call(Integer integer) {
						mTextScan.append(String.valueOf(integer) + "  ");
					}
				}));
		mTextScan.append("\n\n");
		mCompositeSubscription.add(Observable.just(2, 2, 3, 4, 5, 6, 7, 8, 9)
				.scan("初始"/*第一次会发射这个数据，之后才会发射处理过后的数据*/, new Func2<String, Integer, String>() {
					@Override
					public String call(String string, Integer integer) {
						return string + "-" + String.valueOf(integer.intValue());
					}
				})
				.subscribe(new Action1<String>() {
					@Override
					public void call(String string) {
						mTextScan.append(string + "  ");
					}
				}));
	}

	private void cast() {// 强转
		RxTextView.text(mTextCast).call("");
		Object object = new Student();
		mCompositeSubscription.add(Observable.just(object)
				.cast(Student.class)
				.map(new Func1<Student, Student>() {
					@Override
					public Student call(Student student) {
						student.setName("cast");
						int count = (int) (Math.random() * 5 + 1);
						Student.Course[] courses = new Student.Course[count];
						for (int i = 0; i < count; i++) {
							courses[i] = new Student.Course("course-" + i);
						}
						student.setCourses(courses);
						return student;
					}
				})
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Action1<Student>() {
					@Override
					public void call(Student student) {
						RxTextView.text(mTextCast).call(student.getName() + "：");
						Student.Course[] courses = student.getCourses();
						for (Student.Course course : courses) {
							mTextCast.append(course.getName() + "  ");
						}
					}
				}));
	}

	private void flatMap() {// 将每个学生的所有课程名打印出来
		RxTextView.text(mTextFlatMap).call("");
		Student[] students = new Student[(int) (Math.random() * 10 + 1)];
		for (int i = 0; i < students.length; i++) {
			students[i] = new Student();
			students[i].setName("name_" + i);
			int num = (int) (Math.random() * 10 + 1);
			Student.Course[] courses = new Student.Course[num];
			for (int j = 0; j < num; j++) {
				courses[j] = new Student.Course(students[i].getName() + "：" + "course" + j);
			}
			students[i].setCourses(courses);
		}
		mCompositeSubscription.add(Observable.from(students)
				.flatMap(new Func1<Student, Observable<Student.Course>>() {
					@Override
					public Observable<Student.Course> call(Student student) {
						return Observable.from(student.getCourses());
					}
				})
				.limit(24)
				.subscribe(new Action1<Student.Course>() {
					@Override
					public void call(Student.Course course) {
						mTextFlatMap.append(course.getName() + "\n");
					}
				}));
	}

	private void map() {// 将指定数据类型转换为另外一种类型
		RxTextView.text(mTextMap).call("");
		mCompositeSubscription.add(Observable.range((int) (Math.random() * 255 + 1), (int) (Math.random() * 10 + 1))
				.map(new Func1<Integer, Character>() {
					@Override
					public Character call(Integer integer) {
						return (char) integer.intValue();
					}
				})
				.map(new Func1<Character, String>() {
					@Override
					public String call(Character character) {
						return String.valueOf(character.charValue());
					}
				})
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Action1<String>() {
					@Override
					public void call(String string) {
						mTextMap.append(string + "  ");
					}
				}));
	}

	private void lift() {// 用自己定义的lift工作，针对事件项和事件序列转换，官方建议最好不要自定义，用已有的就行，比如：map，flatMap
		mCompositeSubscription.add(Observable.just(8)
				.lift(new Observable.Operator<String, Integer>() {
					@Override
					public Subscriber<? super Integer> call(final Subscriber<? super String> subscriber) {
						return new Subscriber<Integer>() {
							@Override
							public void onCompleted() {
								subscriber.onCompleted();
							}

							@Override
							public void onError(Throwable e) {
								subscriber.onError(e);
							}

							@Override
							public void onNext(Integer integer) {
								subscriber.onNext("string：" + String.valueOf(integer));
							}
						};
					}
				})
				.subscribeOn(Schedulers.newThread())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Action1<String>() {
					@Override
					public void call(String string) {
						RxTextView.text(mTextLift).call(string);
					}
				}));
	}

	private void schedulers() {// io线程构造一个Drawable，回调到UI线程在ImageView上显示
		mCompositeSubscription.add(Observable
				.create(new Observable.OnSubscribe<Drawable>() {
					@Override
					public void call(Subscriber<? super Drawable> subscriber) {
						Drawable drawable = getResources().getDrawable(R.mipmap.ic_launcher);
						subscriber.onNext(drawable);
						subscriber.onCompleted();
					}
				})
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Drawable>() {
					@Override
					public void onCompleted() {
					}

					@Override
					public void onError(Throwable e) {
						Toast.makeText(RxJavaActivity.this, "Error!", Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onNext(Drawable drawable) {
						mImageSchedulers.setImageDrawable(drawable);
					}
				}));
	}

	private void schedulePeriodically(View view) {// 轮询，事情做了回调
		RxView.enabled(view).call(false);
		RxTextView.text(mTextSchedulePeriodically).call("");
		mCompositeSubscription.add(Observable
				.create(new Observable.OnSubscribe<String>() {
					@Override
					public void call(final Subscriber<? super String> observer) {
						mCompositeSubscription.add(Schedulers.newThread().createWorker()
								.schedulePeriodically(new Action0() {
									@Override
									public void call() {
										observer.onNext("轮询 - " + System.currentTimeMillis() + "\n");
									}
								}, 0, 5, TimeUnit.SECONDS));
					}
				})
				.observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<String>() {
					@Override
					public void call(String string) {
						if (mTextSchedulePeriodically.getLineCount() == 4) RxTextView.text(mTextSchedulePeriodically).call("");
						mTextSchedulePeriodically.append(string);
					}
				}));
	}

	private void windowTime(View view) {// 与buffer类似，window发射的是一些小的Observable对象
		RxView.enabled(view).call(false);
		RxTextView.text(mTextWindowTime).call("");
		mCompositeSubscription.add(Observable.interval(1, TimeUnit.SECONDS)
				.window(3, TimeUnit.SECONDS)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Action1<Observable<Long>>() {
					@Override
					public void call(Observable<Long> longObservable) {
						if (!TextUtils.isEmpty(mTextWindowTime.getText().toString())) mTextWindowTime.append("\n");
						longObservable
								.observeOn(AndroidSchedulers.mainThread())
								.subscribe(new Action1<Long>() {
									@Override
									public void call(Long lg) {
										mTextWindowTime.append(lg + "s  ");
										if (mTextWindowTime.getLineCount() == 4) {
											RxTextView.text(mTextWindowTime).call("");
											mTextWindowTime.append(lg + "s  ");
										}
									}
								});
					}
				}));
	}

	private void window() {// 与buffer类似，window发射的是一些小的Observable对象
		RxTextView.text(mTextWindow).call("");
		mCompositeSubscription.add(Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9)
				.window(2, 2)
				.subscribe(new Action1<Observable<Integer>>() {
					@Override
					public void call(Observable<Integer> integerObservable) {
						if (!TextUtils.isEmpty(mTextWindow.getText().toString())) mTextWindow.append("-  ");
						integerObservable.subscribe(new Action1<Integer>() {
							@Override
							public void call(Integer integer) {
								mTextWindow.append(integer + "  ");
							}
						});
					}
				}));
	}

	private void bufferTime(View view) {// 缓存够指定时间之后发射
		RxView.enabled(view).call(false);
		RxTextView.text(mTextBufferTime).call("");
		mCompositeSubscription.add(Observable.interval(1, TimeUnit.SECONDS)
				.buffer(3, TimeUnit.SECONDS)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Action1<List<Long>>() {
					@Override
					public void call(List<Long> longs) {
						if (!TextUtils.isEmpty(mTextBufferTime.getText().toString())) mTextBufferTime.append("\n");
						for (Long lg : longs) {
							mTextBufferTime.append(lg + "s  ");
						}
						if (mTextBufferTime.getLineCount() == 4) {
							RxTextView.text(mTextBufferTime).call("");
							for (Long lg : longs) {
								mTextBufferTime.append(lg + "s  ");
							}
						}
					}
				}));
	}

	private void buffer() {// 缓存够指定数量之后才发射
		RxTextView.text(mTextBuffer).call("");
		mCompositeSubscription.add(Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9)
				.buffer(2, 3)
				.subscribe(new Action1<List<Integer>>() {
					@Override
					public void call(List<Integer> integers) {
						if (!TextUtils.isEmpty(mTextBuffer.getText().toString())) mTextBuffer.append("-  ");
						for (Integer integer : integers) {
							mTextBuffer.append(integer + "  ");
						}
					}
				}));
	}

	private void interval(View view) {// 定时处理，回调了才做事情，第一次不会立即执行
		RxView.enabled(view).call(false);
		RxTextView.text(mTextInterval).call("定时 - " + String.valueOf(System.currentTimeMillis()) + "\n");
		mCompositeSubscription.add(Observable.interval(2, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
				.subscribe(new Action1<Long>() {
					@Override
					public void call(Long lg) {
						if (mTextInterval.getLineCount() == 4) RxTextView.text(mTextInterval).call("");
						mTextInterval.append("定时 - " + String.valueOf(System.currentTimeMillis()) + "\n");
					}
				}));
	}

	private void repeat() {// 将一个Observable对象重复发送
		RxTextView.text(mTextRepeat).call("");
		mCompositeSubscription.add(Observable.just("repeat")
				.repeat((long) (Math.random() * 10 + 1))
				.subscribe(new Action1<String>() {
					@Override
					public void call(String string) {
						mTextRepeat.append(string + "  ");
					}
				}));
	}

	private void timer(View view) {// 延时处理
		RxView.enabled(view).call(false);
		RxTextView.text(mTextTimer).call(String.valueOf(System.currentTimeMillis()));
		mCompositeSubscription.add(Observable.timer(2, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
				.subscribe(new Action1<Long>() {
					@Override
					public void call(Long lg) {
						mTextTimer.append("\n" + String.valueOf(System.currentTimeMillis()));
					}
				}));
	}

	private void rang() {// 生成一组范围数据
		RxTextView.text(mTextRang).call("");
		mCompositeSubscription.add(Observable.range((int) (Math.random() * 100 + 1), (int) (Math.random() * 10 + 1))
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Action1<Integer>() {
					@Override
					public void call(Integer integer) {
						mTextRang.append(String.valueOf(integer.intValue()) + "  ");
					}
				}));
	}

	private void from() {// 数据来源为一个数组
		RxTextView.text(mTextFrom).call("");
		mCompositeSubscription.add(Observable.from(new String[]{"Hello", "Hi", "Nice"})
				.subscribe(new Action1<String>() {
					@Override
					public void call(String string) {
						mTextFrom.append(string + "  ");
					}
				}));
	}

	Observable<Long> deferObservable = Observable.defer(new Func0<Observable<Long>>() {
		@Override
		public Observable<Long> call() {
			return Observable.just(System.currentTimeMillis());
		}
	});

	private void defer() {// 只有当有Subscriber来订阅的时候才会创建一个新的Observable对象
		mCompositeSubscription.add(deferObservable.subscribe(new Action1<Long>() {
			@Override
			public void call(Long lg) {
				if (mTextDefer.getLineCount() == 4) RxTextView.text(mTextDefer).call("");
				mTextDefer.append(lg + "\n");
			}
		}));
	}

	Observable<Long> justObservable = Observable.just(System.currentTimeMillis());

	private void just() {// 将某个对象转化为Observable对象，并且将其发射出去
		mCompositeSubscription.add(justObservable.subscribe(new Action1<Long>() {
			@Override
			public void call(Long lg) {
				if (mTextJust.getLineCount() == 4) RxTextView.text(mTextJust).call("");
				mTextJust.append(lg + "\n");
			}
		}));
	}

	private void create() {// 最简单的一个例子
		RxTextView.text(mTextCreate).call("");
		mCompositeSubscription.add(Observable.create(new Observable.OnSubscribe<String>() {
			@Override
			public void call(Subscriber<? super String> subscriber) {
				subscriber.onNext("Hello");
				subscriber.onNext("Hi");
				subscriber.onNext("Nice");
				subscriber.onCompleted();
			}
		}).subscribe(new Subscriber<String>() {
			@Override
			public void onCompleted() {
				mTextCreate.append("onCompleted");
			}

			@Override
			public void onError(Throwable e) {
				mTextCreate.append("onError：" + e.getMessage());
			}

			@Override
			public void onNext(String string) {
				mTextCreate.append(string + "  ");
			}
		}));
	}
}

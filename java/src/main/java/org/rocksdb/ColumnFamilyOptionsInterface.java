// Copyright (c) 2011-present, Facebook, Inc.  All rights reserved.
// This source code is licensed under the BSD-style license found in the
// LICENSE file in the root directory of this source tree. An additional grant
// of patent rights can be found in the PATENTS file in the same directory.

package org.rocksdb;

import java.util.List;

public interface ColumnFamilyOptionsInterface {

  /**
   * Use this if you don't need to keep the data sorted, i.e. you'll never use
   * an iterator, only Put() and Get() API calls
   *
   * @param blockCacheSizeMb Block cache size in MB
   * @return the instance of the current Object.
   */
  ColumnFamilyOptionsInterface optimizeForPointLookup(long blockCacheSizeMb);

  /**
   * <p>Default values for some parameters in ColumnFamilyOptions are not
   * optimized for heavy workloads and big datasets, which means you might
   * observe write stalls under some conditions. As a starting point for tuning
   * RocksDB options, use the following for level style compaction.</p>
   *
   * <p>Make sure to also call IncreaseParallelism(), which will provide the
   * biggest performance gains.</p>
   * <p>Note: we might use more memory than memtable_memory_budget during high
   * write rate period</p>
   *
   * @return the instance of the current Object.
   */
  ColumnFamilyOptionsInterface optimizeLevelStyleCompaction();

  /**
   * <p>Default values for some parameters in ColumnFamilyOptions are not
   * optimized for heavy workloads and big datasets, which means you might
   * observe write stalls under some conditions. As a starting point for tuning
   * RocksDB options, use the following for level style compaction.</p>
   *
   * <p>Make sure to also call IncreaseParallelism(), which will provide the
   * biggest performance gains.</p>
   * <p>Note: we might use more memory than memtable_memory_budget during high
   * write rate period</p>
   *
   * @param memtableMemoryBudget memory budget in bytes
   * @return the instance of the current Object.
   */
  Object optimizeLevelStyleCompaction(long memtableMemoryBudget);

  /**
   * <p>Default values for some parameters in ColumnFamilyOptions are not
   * optimized for heavy workloads and big datasets, which means you might
   * observe write stalls under some conditions. As a starting point for tuning
   * RocksDB options, use the following for universal style compaction.</p>
   *
   * <p>Universal style compaction is focused on reducing Write Amplification
   * Factor for big data sets, but increases Space Amplification.</p>
   *
   * <p>Make sure to also call IncreaseParallelism(), which will provide the
   * biggest performance gains.</p>
   *
   * <p>Note: we might use more memory than memtable_memory_budget during high
   * write rate period</p>
   *
   * @return the instance of the current Object.
   */
  Object optimizeUniversalStyleCompaction();

  /**
   * <p>Default values for some parameters in ColumnFamilyOptions are not
   * optimized for heavy workloads and big datasets, which means you might
   * observe write stalls under some conditions. As a starting point for tuning
   * RocksDB options, use the following for universal style compaction.</p>
   *
   * <p>Universal style compaction is focused on reducing Write Amplification
   * Factor for big data sets, but increases Space Amplification.</p>
   *
   * <p>Make sure to also call IncreaseParallelism(), which will provide the
   * biggest performance gains.</p>
   *
   * <p>Note: we might use more memory than memtable_memory_budget during high
   * write rate period</p>
   *
   * @param memtableMemoryBudget memory budget in bytes
   * @return the instance of the current Object.
   */
  Object optimizeUniversalStyleCompaction(long memtableMemoryBudget);

  /**
   * Set {@link BuiltinComparator} to be used with RocksDB.
   *
   * Note: Comparator can be set once upon database creation.
   *
   * Default: BytewiseComparator.
   * @param builtinComparator a {@link BuiltinComparator} type.
   * @return the instance of the current Object.
   */
  Object setComparator(BuiltinComparator builtinComparator);

  /**
   * Use the specified comparator for key ordering.
   *
   * Comparator should not be disposed before options instances using this comparator is
   * disposed. If dispose() function is not called, then comparator object will be
   * GC'd automatically.
   *
   * Comparator instance can be re-used in multiple options instances.
   *
   * @param comparator java instance.
   * @return the instance of the current Object.
   */
  Object setComparator(AbstractComparator<? extends AbstractSlice<?>> comparator);

  /**
   * <p>Set the merge operator to be used for merging two merge operands
   * of the same key. The merge function is invoked during
   * compaction and at lookup time, if multiple key/value pairs belonging
   * to the same key are found in the database.</p>
   *
   * @param name the name of the merge function, as defined by
   * the MergeOperators factory (see utilities/MergeOperators.h)
   * The merge function is specified by name and must be one of the
   * standard merge operators provided by RocksDB. The available
   * operators are "put", "uint64add", "stringappend" and "stringappendtest".
   * @return the instance of the current Object.
   */
  Object setMergeOperatorName(String name);

  /**
   * <p>Set the merge operator to be used for merging two different key/value
   * pairs that share the same key. The merge function is invoked during
   * compaction and at lookup time, if multiple key/value pairs belonging
   * to the same key are found in the database.</p>
   *
   * @param mergeOperator {@link MergeOperator} instance.
   * @return the instance of the current Object.
   */
  Object setMergeOperator(MergeOperator mergeOperator);

  /**
   * The minimum number of write buffers that will be merged together
   * before writing to storage.  If set to 1, then
   * all write buffers are flushed to L0 as individual files and this increases
   * read amplification because a get request has to check in all of these
   * files. Also, an in-memory merge may result in writing lesser
   * data to storage if there are duplicate records in each of these
   * individual write buffers.  Default: 1
   *
   * @param minWriteBufferNumberToMerge the minimum number of write buffers
   *     that will be merged together.
   * @return the reference to the current option.
   */
  Object setMinWriteBufferNumberToMerge(
      int minWriteBufferNumberToMerge);

  /**
   * The minimum number of write buffers that will be merged together
   * before writing to storage.  If set to 1, then
   * all write buffers are flushed to L0 as individual files and this increases
   * read amplification because a get request has to check in all of these
   * files. Also, an in-memory merge may result in writing lesser
   * data to storage if there are duplicate records in each of these
   * individual write buffers.  Default: 1
   *
   * @return the minimum number of write buffers that will be merged together.
   */
  int minWriteBufferNumberToMerge();

  /**
   * This prefix-extractor uses the first n bytes of a key as its prefix.
   *
   * In some hash-based memtable representation such as HashLinkedList
   * and HashSkipList, prefixes are used to partition the keys into
   * several buckets.  Prefix extractor is used to specify how to
   * extract the prefix given a key.
   *
   * @param n use the first n bytes of a key as its prefix.
   * @return the reference to the current option.
   */
  Object useFixedLengthPrefixExtractor(int n);


  /**
   * Same as fixed length prefix extractor, except that when slice is
   * shorter than the fixed length, it will use the full key.
   *
   * @param n use the first n bytes of a key as its prefix.
   * @return the reference to the current option.
   */
  Object useCappedPrefixExtractor(int n);

  /**
   * Compress blocks using the specified compression algorithm.  This
   * parameter can be changed dynamically.
   *
   * Default: SNAPPY_COMPRESSION, which gives lightweight but fast compression.
   *
   * @param compressionType Compression Type.
   * @return the reference to the current option.
   */
  Object setCompressionType(CompressionType compressionType);

  /**
   * Compress blocks using the specified compression algorithm.  This
   * parameter can be changed dynamically.
   *
   * Default: SNAPPY_COMPRESSION, which gives lightweight but fast compression.
   *
   * @return Compression type.
   */
  CompressionType compressionType();

  /**
   * <p>Different levels can have different compression
   * policies. There are cases where most lower levels
   * would like to use quick compression algorithms while
   * the higher levels (which have more data) use
   * compression algorithms that have better compression
   * but could be slower. This array, if non-empty, should
   * have an entry for each level of the database;
   * these override the value specified in the previous
   * field 'compression'.</p>
   *
   * <strong>NOTICE</strong>
   * <p>If {@code level_compaction_dynamic_level_bytes=true},
   * {@code compression_per_level[0]} still determines {@code L0},
   * but other elements of the array are based on base level
   * (the level {@code L0} files are merged to), and may not
   * match the level users see from info log for metadata.
   * </p>
   * <p>If {@code L0} files are merged to {@code level - n},
   * then, for {@code i&gt;0}, {@code compression_per_level[i]}
   * determines compaction type for level {@code n+i-1}.</p>
   *
   * <strong>Example</strong>
   * <p>For example, if we have 5 levels, and we determine to
   * merge {@code L0} data to {@code L4} (which means {@code L1..L3}
   * will be empty), then the new files go to {@code L4} uses
   * compression type {@code compression_per_level[1]}.</p>
   *
   * <p>If now {@code L0} is merged to {@code L2}. Data goes to
   * {@code L2} will be compressed according to
   * {@code compression_per_level[1]}, {@code L3} using
   * {@code compression_per_level[2]}and {@code L4} using
   * {@code compression_per_level[3]}. Compaction for each
   * level can change when data grows.</p>
   *
   * <p><strong>Default:</strong> empty</p>
   *
   * @param compressionLevels list of
   *     {@link org.rocksdb.CompressionType} instances.
   *
   * @return the reference to the current option.
   */
  Object setCompressionPerLevel(
      List<CompressionType> compressionLevels);

  /**
   * <p>Return the currently set {@link org.rocksdb.CompressionType}
   * per instances.</p>
   *
   * <p>See: {@link #setCompressionPerLevel(java.util.List)}</p>
   *
   * @return list of {@link org.rocksdb.CompressionType}
   *     instances.
   */
  List<CompressionType> compressionPerLevel();

  /**
   * Set the number of levels for this database
   * If level-styled compaction is used, then this number determines
   * the total number of levels.
   *
   * @param numLevels the number of levels.
   * @return the reference to the current option.
   */
  Object setNumLevels(int numLevels);

  /**
   * If level-styled compaction is used, then this number determines
   * the total number of levels.
   *
   * @return the number of levels.
   */
  int numLevels();

  /**
   * Number of files to trigger level-0 compaction. A value &lt; 0 means that
   * level-0 compaction will not be triggered by number of files at all.
   * Default: 4
   *
   * @param numFiles the number of files in level-0 to trigger compaction.
   * @return the reference to the current option.
   */
  Object setLevelZeroFileNumCompactionTrigger(
      int numFiles);

  /**
   * The number of files in level 0 to trigger compaction from level-0 to
   * level-1.  A value &lt; 0 means that level-0 compaction will not be
   * triggered by number of files at all.
   * Default: 4
   *
   * @return the number of files in level 0 to trigger compaction.
   */
  int levelZeroFileNumCompactionTrigger();

  /**
   * Soft limit on number of level-0 files. We start slowing down writes at this
   * point. A value &lt; 0 means that no writing slow down will be triggered by
   * number of files in level-0.
   *
   * @param numFiles soft limit on number of level-0 files.
   * @return the reference to the current option.
   */
  Object setLevelZeroSlowdownWritesTrigger(
      int numFiles);

  /**
   * Soft limit on the number of level-0 files. We start slowing down writes
   * at this point. A value &lt; 0 means that no writing slow down will be
   * triggered by number of files in level-0.
   *
   * @return the soft limit on the number of level-0 files.
   */
  int levelZeroSlowdownWritesTrigger();

  /**
   * Maximum number of level-0 files.  We stop writes at this point.
   *
   * @param numFiles the hard limit of the number of level-0 files.
   * @return the reference to the current option.
   */
  Object setLevelZeroStopWritesTrigger(int numFiles);

  /**
   * Maximum number of level-0 files.  We stop writes at this point.
   *
   * @return the hard limit of the number of level-0 file.
   */
  int levelZeroStopWritesTrigger();

  /**
   * This does nothing anymore. Deprecated.
   *
   * @param maxMemCompactionLevel Unused.
   *
   * @return the reference to the current option.
   */
  @Deprecated
  Object setMaxMemCompactionLevel(
      int maxMemCompactionLevel);

  /**
   * This does nothing anymore. Deprecated.
   *
   * @return Always returns 0.
   */
  @Deprecated
  int maxMemCompactionLevel();

  /**
   * <p>If {@code true}, RocksDB will pick target size of each level
   * dynamically. We will pick a base level b &gt;= 1. L0 will be
   * directly merged into level b, instead of always into level 1.
   * Level 1 to b-1 need to be empty. We try to pick b and its target
   * size so that</p>
   *
   * <ol>
   * <li>target size is in the range of
   *   (max_bytes_for_level_base / max_bytes_for_level_multiplier,
   *    max_bytes_for_level_base]</li>
   * <li>target size of the last level (level num_levels-1) equals to extra size
   *    of the level.</li>
   * </ol>
   *
   * <p>At the same time max_bytes_for_level_multiplier and
   * max_bytes_for_level_multiplier_additional are still satisfied.</p>
   *
   * <p>With this option on, from an empty DB, we make last level the base
   * level, which means merging L0 data into the last level, until it exceeds
   * max_bytes_for_level_base. And then we make the second last level to be
   * base level, to start to merge L0 data to second last level, with its
   * target size to be {@code 1/max_bytes_for_level_multiplier} of the last
   * levels extra size. After the data accumulates more so that we need to
   * move the base level to the third last one, and so on.</p>
   *
   * <h2>Example</h2>
   * <p>For example, assume {@code max_bytes_for_level_multiplier=10},
   * {@code num_levels=6}, and {@code max_bytes_for_level_base=10MB}.</p>
   *
   * <p>Target sizes of level 1 to 5 starts with:</p>
   * {@code [- - - - 10MB]}
   * <p>with base level is level. Target sizes of level 1 to 4 are not applicable
   * because they will not be used.
   * Until the size of Level 5 grows to more than 10MB, say 11MB, we make
   * base target to level 4 and now the targets looks like:</p>
   * {@code [- - - 1.1MB 11MB]}
   * <p>While data are accumulated, size targets are tuned based on actual data
   * of level 5. When level 5 has 50MB of data, the target is like:</p>
   * {@code [- - - 5MB 50MB]}
   * <p>Until level 5's actual size is more than 100MB, say 101MB. Now if we
   * keep level 4 to be the base level, its target size needs to be 10.1MB,
   * which doesn't satisfy the target size range. So now we make level 3
   * the target size and the target sizes of the levels look like:</p>
   * {@code [- - 1.01MB 10.1MB 101MB]}
   * <p>In the same way, while level 5 further grows, all levels' targets grow,
   * like</p>
   * {@code [- - 5MB 50MB 500MB]}
   * <p>Until level 5 exceeds 1000MB and becomes 1001MB, we make level 2 the
   * base level and make levels' target sizes like this:</p>
   * {@code [- 1.001MB 10.01MB 100.1MB 1001MB]}
   * <p>and go on...</p>
   *
   * <p>By doing it, we give {@code max_bytes_for_level_multiplier} a priority
   * against {@code max_bytes_for_level_base}, for a more predictable LSM tree
   * shape. It is useful to limit worse case space amplification.</p>
   *
   * <p>{@code max_bytes_for_level_multiplier_additional} is ignored with
   * this flag on.</p>
   *
   * <p>Turning this feature on or off for an existing DB can cause unexpected
   * LSM tree structure so it's not recommended.</p>
   *
   * <p><strong>Caution</strong>: this option is experimental</p>
   *
   * <p>Default: false</p>
   *
   * @param enableLevelCompactionDynamicLevelBytes boolean value indicating
   *     if {@code LevelCompactionDynamicLevelBytes} shall be enabled.
   * @return the reference to the current option.
   */
  Object setLevelCompactionDynamicLevelBytes(
      boolean enableLevelCompactionDynamicLevelBytes);

  /**
   * <p>Return if {@code LevelCompactionDynamicLevelBytes} is enabled.
   * </p>
   *
   * <p>For further information see
   * {@link #setLevelCompactionDynamicLevelBytes(boolean)}</p>
   *
   * @return boolean value indicating if
   *    {@code levelCompactionDynamicLevelBytes} is enabled.
   */
  boolean levelCompactionDynamicLevelBytes();

  /**
   * The ratio between the total size of level-(L+1) files and the total
   * size of level-L files for all L.
   * DEFAULT: 10
   *
   * @param multiplier the ratio between the total size of level-(L+1)
   *     files and the total size of level-L files for all L.
   * @return the reference to the current option.
   */
  Object setMaxBytesForLevelMultiplier(double multiplier);

  /**
   * The ratio between the total size of level-(L+1) files and the total
   * size of level-L files for all L.
   * DEFAULT: 10
   *
   * @return the ratio between the total size of level-(L+1) files and
   *     the total size of level-L files for all L.
   */
  double maxBytesForLevelMultiplier();

  /**
   * Maximum size of each compaction (not guarantee)
   *
   * @param maxCompactionBytes the compaction size limit
   * @return the reference to the current option.
   */
  Object setMaxCompactionBytes(long maxCompactionBytes);

  /**
   * Control maximum size of each compaction (not guaranteed)
   *
   * @return compaction size threshold
   */
  long maxCompactionBytes();

  /**
   * Puts are delayed 0-1 ms when any level has a compaction score that exceeds
   * soft_rate_limit. This is ignored when == 0.0.
   * CONSTRAINT: soft_rate_limit &le; hard_rate_limit. If this constraint does not
   * hold, RocksDB will set soft_rate_limit = hard_rate_limit
   * Default: 0 (disabled)
   *
   * @param softRateLimit the soft-rate-limit of a compaction score
   *     for put delay.
   * @return the reference to the current option.
   */
  Object setSoftRateLimit(double softRateLimit);

  /**
   * Puts are delayed 0-1 ms when any level has a compaction score that exceeds
   * soft_rate_limit. This is ignored when == 0.0.
   * CONSTRAINT: soft_rate_limit &le; hard_rate_limit. If this constraint does not
   * hold, RocksDB will set soft_rate_limit = hard_rate_limit
   * Default: 0 (disabled)
   *
   * @return soft-rate-limit for put delay.
   */
  double softRateLimit();

  /**
   * Puts are delayed 1ms at a time when any level has a compaction score that
   * exceeds hard_rate_limit. This is ignored when &le; 1.0.
   * Default: 0 (disabled)
   *
   * @param hardRateLimit the hard-rate-limit of a compaction score for put
   *     delay.
   * @return the reference to the current option.
   */
  Object setHardRateLimit(double hardRateLimit);

  /**
   * Puts are delayed 1ms at a time when any level has a compaction score that
   * exceeds hard_rate_limit. This is ignored when &le; 1.0.
   * Default: 0 (disabled)
   *
   * @return the hard-rate-limit of a compaction score for put delay.
   */
  double hardRateLimit();

  /**
   * The maximum time interval a put will be stalled when hard_rate_limit
   * is enforced. If 0, then there is no limit.
   * Default: 1000
   *
   * @param rateLimitDelayMaxMilliseconds the maximum time interval a put
   *     will be stalled.
   * @return the reference to the current option.
   */
  Object setRateLimitDelayMaxMilliseconds(
      int rateLimitDelayMaxMilliseconds);

  /**
   * The maximum time interval a put will be stalled when hard_rate_limit
   * is enforced.  If 0, then there is no limit.
   * Default: 1000
   *
   * @return the maximum time interval a put will be stalled when
   *     hard_rate_limit is enforced.
   */
  int rateLimitDelayMaxMilliseconds();

  /**
   * Purge duplicate/deleted keys when a memtable is flushed to storage.
   * Default: true
   *
   * @param purgeRedundantKvsWhileFlush true if purging keys is disabled.
   * @return the reference to the current option.
   */
  Object setPurgeRedundantKvsWhileFlush(
      boolean purgeRedundantKvsWhileFlush);

  /**
   * Purge duplicate/deleted keys when a memtable is flushed to storage.
   * Default: true
   *
   * @return true if purging keys is disabled.
   */
  boolean purgeRedundantKvsWhileFlush();

  /**
   * Set compaction style for DB.
   *
   * Default: LEVEL.
   *
   * @param compactionStyle Compaction style.
   * @return the reference to the current option.
   */
  Object setCompactionStyle(CompactionStyle compactionStyle);

  /**
   * Compaction style for DB.
   *
   * @return Compaction style.
   */
  CompactionStyle compactionStyle();

  /**
   * FIFO compaction option.
   * The oldest table file will be deleted
   * once the sum of table files reaches this size.
   * The default value is 1GB (1 * 1024 * 1024 * 1024).
   *
   * @param maxTableFilesSize the size limit of the total sum of table files.
   * @return the instance of the current Object.
   */
  Object setMaxTableFilesSizeFIFO(long maxTableFilesSize);

  /**
   * FIFO compaction option.
   * The oldest table file will be deleted
   * once the sum of table files reaches this size.
   * The default value is 1GB (1 * 1024 * 1024 * 1024).
   *
   * @return the size limit of the total sum of table files.
   */
  long maxTableFilesSizeFIFO();

  /**
   * Set the config for mem-table.
   *
   * @param config the mem-table config.
   * @return the instance of the current Object.
   * @throws java.lang.IllegalArgumentException thrown on 32-Bit platforms
   *   while overflowing the underlying platform specific value.
   */
  Object setMemTableConfig(MemTableConfig config);

  /**
   * Returns the name of the current mem table representation.
   * Memtable format can be set using setTableFormatConfig.
   *
   * @return the name of the currently-used memtable factory.
   * @see #setTableFormatConfig(org.rocksdb.TableFormatConfig)
   */
  String memTableFactoryName();

  /**
   * Set the config for table format.
   *
   * @param config the table format config.
   * @return the reference of the current Options.
   */
  Object setTableFormatConfig(TableFormatConfig config);

  /**
   * @return the name of the currently used table factory.
   */
  String tableFactoryName();

  /**
   * Allows thread-safe inplace updates.
   * If inplace_callback function is not set,
   *   Put(key, new_value) will update inplace the existing_value iff
   *   * key exists in current memtable
   *   * new sizeof(new_value) &le; sizeof(existing_value)
   *   * existing_value for that key is a put i.e. kTypeValue
   * If inplace_callback function is set, check doc for inplace_callback.
   * Default: false.
   *
   * @param inplaceUpdateSupport true if thread-safe inplace updates
   *     are allowed.
   * @return the reference to the current option.
   */
  Object setInplaceUpdateSupport(boolean inplaceUpdateSupport);

  /**
   * Allows thread-safe inplace updates.
   * If inplace_callback function is not set,
   *   Put(key, new_value) will update inplace the existing_value iff
   *   * key exists in current memtable
   *   * new sizeof(new_value) &le; sizeof(existing_value)
   *   * existing_value for that key is a put i.e. kTypeValue
   * If inplace_callback function is set, check doc for inplace_callback.
   * Default: false.
   *
   * @return true if thread-safe inplace updates are allowed.
   */
  boolean inplaceUpdateSupport();

  /**
   * Control locality of bloom filter probes to improve cache miss rate.
   * This option only applies to memtable prefix bloom and plaintable
   * prefix bloom. It essentially limits the max number of cache lines each
   * bloom filter check can touch.
   * This optimization is turned off when set to 0. The number should never
   * be greater than number of probes. This option can boost performance
   * for in-memory workload but should use with care since it can cause
   * higher false positive rate.
   * Default: 0
   *
   * @param bloomLocality the level of locality of bloom-filter probes.
   * @return the reference to the current option.
   */
  Object setBloomLocality(int bloomLocality);

  /**
   * Control locality of bloom filter probes to improve cache miss rate.
   * This option only applies to memtable prefix bloom and plaintable
   * prefix bloom. It essentially limits the max number of cache lines each
   * bloom filter check can touch.
   * This optimization is turned off when set to 0. The number should never
   * be greater than number of probes. This option can boost performance
   * for in-memory workload but should use with care since it can cause
   * higher false positive rate.
   * Default: 0
   *
   * @return the level of locality of bloom-filter probes.
   * @see #setBloomLocality(int)
   */
  int bloomLocality();

  /**
   * The number of partial merge operands to accumulate before partial
   * merge will be performed. Partial merge will not be called
   * if the list of values to merge is less than min_partial_merge_operands.
   *
   * If min_partial_merge_operands &lt; 2, then it will be treated as 2.
   *
   * Default: 2
   *
   * @param minPartialMergeOperands min partial merge operands
   * @return the reference to the current option.
   */
  Object setMinPartialMergeOperands(int minPartialMergeOperands);

  /**
   * The number of partial merge operands to accumulate before partial
   * merge will be performed. Partial merge will not be called
   * if the list of values to merge is less than min_partial_merge_operands.
   *
   * If min_partial_merge_operands &lt; 2, then it will be treated as 2.
   *
   * Default: 2
   *
   * @return min partial merge operands
   */
  int minPartialMergeOperands();

  /**
   * <p>This flag specifies that the implementation should optimize the filters
   * mainly for cases where keys are found rather than also optimize for keys
   * missed. This would be used in cases where the application knows that
   * there are very few misses or the performance in the case of misses is not
   * important.</p>
   *
   * <p>For now, this flag allows us to not store filters for the last level i.e
   * the largest level which contains data of the LSM store. For keys which
   * are hits, the filters in this level are not useful because we will search
   * for the data anyway.</p>
   *
   * <p><strong>NOTE</strong>: the filters in other levels are still useful
   * even for key hit because they tell us whether to look in that level or go
   * to the higher level.</p>
   *
   * <p>Default: false<p>
   *
   * @param optimizeFiltersForHits boolean value indicating if this flag is set.
   * @return the reference to the current option.
   */
  Object setOptimizeFiltersForHits(boolean optimizeFiltersForHits);

  /**
   * <p>Returns the current state of the {@code optimize_filters_for_hits}
   * setting.</p>
   *
   * @return boolean value indicating if the flag
   *     {@code optimize_filters_for_hits} was set.
   */
  boolean optimizeFiltersForHits();

  /**
   * Default memtable memory budget used with the following methods:
   *
   * <ol>
   *   <li>{@link #optimizeLevelStyleCompaction()}</li>
   *   <li>{@link #optimizeUniversalStyleCompaction()}</li>
   * </ol>
   */
  long DEFAULT_COMPACTION_MEMTABLE_MEMORY_BUDGET = 512 * 1024 * 1024;
}

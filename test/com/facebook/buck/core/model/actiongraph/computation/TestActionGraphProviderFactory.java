/*
 * Copyright 2018-present Facebook, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.facebook.buck.core.model.actiongraph.computation;

import com.facebook.buck.rules.keys.config.RuleKeyConfiguration;
import com.facebook.buck.rules.keys.config.TestRuleKeyConfigurationFactory;
import com.facebook.buck.util.CloseableMemoizedSupplier;
import java.util.concurrent.ForkJoinPool;

public class TestActionGraphProviderFactory {
  public static ActionGraphProvider create(int maxEntries) {
    CloseableMemoizedSupplier<ForkJoinPool> poolSupplier =
        CloseableMemoizedSupplier.of(
            () -> {
              throw new IllegalStateException(
                  "should not use parallel executor for action graph construction in test");
            },
            ignored -> {});
    return new ActionGraphProvider(
        ActionGraphFactory.create(new ParallelActionGraphFactory(poolSupplier)),
        new ActionGraphCache(maxEntries),
        TestRuleKeyConfigurationFactory.create());
  }

  public static ActionGraphProvider create(
      int maxEntries, CloseableMemoizedSupplier<ForkJoinPool> poolSupplier) {
    return new ActionGraphProvider(
        ActionGraphFactory.create(new ParallelActionGraphFactory(poolSupplier)),
        new ActionGraphCache(maxEntries),
        TestRuleKeyConfigurationFactory.create());
  }

  public static ActionGraphProvider create(
      int maxEntries,
      CloseableMemoizedSupplier<ForkJoinPool> poolSupplier,
      RuleKeyConfiguration ruleKeyConfiguration) {
    return new ActionGraphProvider(
        ActionGraphFactory.create(new ParallelActionGraphFactory(poolSupplier)),
        new ActionGraphCache(maxEntries),
        ruleKeyConfiguration);
  }

  public static ActionGraphProvider create(
      int maxEntries, RuleKeyConfiguration ruleKeyConfiguration) {
    CloseableMemoizedSupplier<ForkJoinPool> poolSupplier =
        CloseableMemoizedSupplier.of(
            () -> {
              throw new IllegalStateException(
                  "should not use parallel executor for action graph construction in test");
            },
            ignored -> {});
    return new ActionGraphProvider(
        ActionGraphFactory.create(new ParallelActionGraphFactory(poolSupplier)),
        new ActionGraphCache(maxEntries),
        ruleKeyConfiguration);
  }
}
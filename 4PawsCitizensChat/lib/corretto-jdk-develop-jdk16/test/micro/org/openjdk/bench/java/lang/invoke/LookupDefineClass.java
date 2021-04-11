/*
 * Copyright (c) 2020, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package org.openjdk.bench.java.lang.invoke;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.concurrent.TimeUnit;

import static java.lang.invoke.MethodHandles.Lookup.ClassOption.*;

public class LookupDefineClass {
    private static final byte[] X_BYTECODE = new byte[]{
            (byte)0xCA, (byte)0xFE, (byte)0xBA, (byte)0xBE, 0x00, 0x00, 0x00, 0x38, 0x00, 0x10, 0x0A, 0x00,
            0x03, 0x00, 0x0C, 0x07, 0x00, 0x0D, 0x07, 0x00, 0x0E, 0x07, 0x00, 0x0F,
            0x01, 0x00, 0x06, 0x3C, 0x69, 0x6E, 0x69, 0x74, 0x3E, 0x01, 0x00, 0x03,
            0x28, 0x29, 0x56, 0x01, 0x00, 0x04, 0x43, 0x6F, 0x64, 0x65, 0x01, 0x00,
            0x0F, 0x4C, 0x69, 0x6E, 0x65, 0x4E, 0x75, 0x6D, 0x62, 0x65, 0x72, 0x54,
            0x61, 0x62, 0x6C, 0x65, 0x01, 0x00, 0x03, 0x72, 0x75, 0x6E, 0x01, 0x00,
            0x0A, 0x53, 0x6F, 0x75, 0x72, 0x63, 0x65, 0x46, 0x69, 0x6C, 0x65, 0x01,
            0x00, 0x08, 0x46, 0x6F, 0x6F, 0x2E, 0x6A, 0x61, 0x76, 0x61, 0x0C, 0x00,
            0x05, 0x00, 0x06, 0x01, 0x00, 0x07, 0x66, 0x6F, 0x6F, 0x2F, 0x46, 0x6F,
            0x6F, 0x01, 0x00, 0x10, 0x6A, 0x61, 0x76, 0x61, 0x2F, 0x6C, 0x61, 0x6E,
            0x67, 0x2F, 0x4F, 0x62, 0x6A, 0x65, 0x63, 0x74, 0x01, 0x00, 0x12, 0x6A,
            0x61, 0x76, 0x61, 0x2F, 0x6C, 0x61, 0x6E, 0x67, 0x2F, 0x52, 0x75, 0x6E,
            0x6E, 0x61, 0x62, 0x6C, 0x65, 0x00, 0x21, 0x00, 0x02, 0x00, 0x03, 0x00,
            0x01, 0x00, 0x04, 0x00, 0x00, 0x00, 0x02, 0x00, 0x01, 0x00, 0x05, 0x00,
            0x06, 0x00, 0x01, 0x00, 0x07, 0x00, 0x00, 0x00, 0x1D, 0x00, 0x01, 0x00,
            0x01, 0x00, 0x00, 0x00, 0x05, 0x2A, (byte)0xB7, 0x00, 0x01, (byte)0xB1, 0x00, 0x00,
            0x00, 0x01, 0x00, 0x08, 0x00, 0x00, 0x00, 0x06, 0x00, 0x01, 0x00, 0x00,
            0x00, 0x03, 0x00, 0x01, 0x00, 0x09, 0x00, 0x06, 0x00, 0x01, 0x00, 0x07,
            0x00, 0x00, 0x00, 0x19, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x01,
            (byte)0xB1, 0x00, 0x00, 0x00, 0x01, 0x00, 0x08, 0x00, 0x00, 0x00, 0x06, 0x00,
            0x01, 0x00, 0x00, 0x00, 0x04, 0x00, 0x01, 0x00, 0x0A, 0x00, 0x00, 0x00,
            0x02, 0x00, 0x0B
    };

    /**
     * Our own crippled classloader, that can only load a simple class over and over again.
     */
    public static class XLoader extends URLClassLoader {
        public XLoader() {
            super("X-Loader", new URL[0], ClassLoader.getSystemClassLoader());
        }

        @Override
        protected Class<?> findClass(final String name) throws ClassNotFoundException {
            return defineClass(name, X_BYTECODE, 0, X_BYTECODE.length);
        }

        public Class<?> install(final String name, byte[] bytes) {
            return defineClass(name, bytes, 0, bytes.length);
        }
    }

    public static class Loader extends URLClassLoader {
        public Loader(String name) {
            super(name, new URL[0], ClassLoader.getSystemClassLoader());
        }

        public Class<?> install(final String name, byte[] bytes) {
            return defineClass(name, bytes, 0, bytes.length);
        }
    }

    @State(Scope.Thread)
    @Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
    @Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
    @Fork(3)
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public static class OneClassPerLoader {
        ClassLoader loader;
        @Benchmark
        public Class<?> load() throws ClassNotFoundException {
            return Class.forName("foo.Foo", false, loader);
        }

        public static void main(String[] args) throws RunnerException {
            Options opt = new OptionsBuilder()
                    .include(LookupDefineClass.OneClassPerLoader.class.getSimpleName())
                    // .addProfiler(ClassloaderProfiler.class)
                    // .addProfiler(CompilerProfiler.class)
                    .build();

            new Runner(opt).run();
        }

        @Setup(Level.Invocation)
        public void doSetup() {
            loader = new XLoader();
        }
    }

    private static final byte[] FOO_HOST_BYTES = new byte[]{
            (byte)0xCA, (byte)0xFE, (byte)0xBA, (byte)0xBE, 0x00, 0x00, 0x00, 0x33, 0x00, 0x12, 0x01, 0x00,
            0x11, 0x66, 0x6F, 0x6F, 0x2F, 0x41, 0x6E, 0x6F, 0x6E, 0x79, 0x6D, 0x6F,
            0x75, 0x73, 0x48, 0x6F, 0x73, 0x74, 0x07, 0x00, 0x01, 0x01, 0x00, 0x10,
            0x6A, 0x61, 0x76, 0x61, 0x2F, 0x6C, 0x61, 0x6E, 0x67, 0x2F, 0x4F, 0x62,
            0x6A, 0x65, 0x63, 0x74, 0x07, 0x00, 0x03, 0x01, 0x00, 0x06, 0x4C, 0x4F,
            0x4F, 0x4B, 0x55, 0x50, 0x01, 0x00, 0x27, 0x4C, 0x6A, 0x61, 0x76, 0x61,
            0x2F, 0x6C, 0x61, 0x6E, 0x67, 0x2F, 0x69, 0x6E, 0x76, 0x6F, 0x6B, 0x65,
            0x2F, 0x4D, 0x65, 0x74, 0x68, 0x6F, 0x64, 0x48, 0x61, 0x6E, 0x64, 0x6C,
            0x65, 0x73, 0x24, 0x4C, 0x6F, 0x6F, 0x6B, 0x75, 0x70, 0x3B, 0x01, 0x00,
            0x08, 0x3C, 0x63, 0x6C, 0x69, 0x6E, 0x69, 0x74, 0x3E, 0x01, 0x00, 0x03,
            0x28, 0x29, 0x56, 0x01, 0x00, 0x1E, 0x6A, 0x61, 0x76, 0x61, 0x2F, 0x6C,
            0x61, 0x6E, 0x67, 0x2F, 0x69, 0x6E, 0x76, 0x6F, 0x6B, 0x65, 0x2F, 0x4D,
            0x65, 0x74, 0x68, 0x6F, 0x64, 0x48, 0x61, 0x6E, 0x64, 0x6C, 0x65, 0x73,
            0x07, 0x00, 0x09, 0x01, 0x00, 0x06, 0x6C, 0x6F, 0x6F, 0x6B, 0x75, 0x70,
            0x01, 0x00, 0x29, 0x28, 0x29, 0x4C, 0x6A, 0x61, 0x76, 0x61, 0x2F, 0x6C,
            0x61, 0x6E, 0x67, 0x2F, 0x69, 0x6E, 0x76, 0x6F, 0x6B, 0x65, 0x2F, 0x4D,
            0x65, 0x74, 0x68, 0x6F, 0x64, 0x48, 0x61, 0x6E, 0x64, 0x6C, 0x65, 0x73,
            0x24, 0x4C, 0x6F, 0x6F, 0x6B, 0x75, 0x70, 0x3B, 0x0C, 0x00, 0x0B, 0x00,
            0x0C, 0x0A, 0x00, 0x0A, 0x00, 0x0D, 0x0C, 0x00, 0x05, 0x00, 0x06, 0x09,
            0x00, 0x02, 0x00, 0x0F, 0x01, 0x00, 0x04, 0x43, 0x6F, 0x64, 0x65, 0x06,
            0x01, 0x00, 0x02, 0x00, 0x04, 0x00, 0x00, 0x00, 0x01, 0x00, 0x19, 0x00,
            0x05, 0x00, 0x06, 0x00, 0x00, 0x00, 0x01, 0x00, 0x08, 0x00, 0x07, 0x00,
            0x08, 0x00, 0x01, 0x00, 0x11, 0x00, 0x00, 0x00, 0x13, 0x00, 0x01, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x07, (byte)0xB8, 0x00, 0x0E, (byte)0xB3, 0x00, 0x10, (byte)0xB1,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00
    };

    private static MethodHandles.Lookup defineHostClass(Loader loader, String name, byte[] bytes) {
        try {
            Class<?> c = loader.install(name, bytes);
            Field f = c.getDeclaredField("LOOKUP");
            return (MethodHandles.Lookup)f.get(null);
        } catch (NoSuchFieldException|IllegalAccessException e) {
            throw new InternalError(e);
        }
    }

    @State(Scope.Thread)
    @Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
    @Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
    @Fork(3)
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public static class TwoClassPerLoader {
        XLoader loader;
        @Benchmark
        public Class<?> load() throws ClassNotFoundException {
            return Class.forName("foo.Foo", false, loader);
        }

        public static void main(String[] args) throws RunnerException {
            Options opt = new OptionsBuilder()
                    .include(LookupDefineClass.TwoClassPerLoader.class.getSimpleName())
                    // .addProfiler(ClassloaderProfiler.class)
                    // .addProfiler(CompilerProfiler.class)
                    .build();

            new Runner(opt).run();
        }

        @Setup(Level.Invocation)
        public void doSetup() {
            loader = new XLoader();
            loader.install("foo.AnonymousHost", FOO_HOST_BYTES);
        }
    }


    @State(Scope.Thread)
    @Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
    @Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
    @Fork(3)
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public static class WeakClass {
        private static final MethodHandles.Lookup HOST_LOOKUP =
                defineHostClass(new Loader("weak-class-loader"), "foo.AnonymousHost", FOO_HOST_BYTES);

        @Benchmark
        public Class<?> load() throws ClassNotFoundException {
            try {
                return HOST_LOOKUP.defineHiddenClass(X_BYTECODE, false).lookupClass();
            } catch (IllegalAccessException e) {
                throw new InternalError(e);
            }
        }

        public static void main(String[] args) throws RunnerException {
            Options opt = new OptionsBuilder()
                    .include(LookupDefineClass.WeakClass.class.getSimpleName())
                    // .addProfiler(ClassloaderProfiler.class)
                    // .addProfiler(CompilerProfiler.class)
                    .build();

            new Runner(opt).run();
        }
    }

    @State(Scope.Thread)
    @Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
    @Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
    @Fork(3)
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public static class UnsafeAnonymousClass {
        static final sun.misc.Unsafe unsafe = getUnsafe();

        private static final MethodHandles.Lookup lookup =
                defineHostClass(new Loader("anonymous-class-loader"),"foo.AnonymousHost", FOO_HOST_BYTES);

        @SuppressWarnings("removal")
        @Benchmark
        public Class<?> load() throws ClassNotFoundException {
            return unsafe.defineAnonymousClass(lookup.lookupClass(), X_BYTECODE, null);
        }

        public static void main(String[] args) throws RunnerException {
            Options opt = new OptionsBuilder()
                    .include(LookupDefineClass.UnsafeAnonymousClass.class.getSimpleName())
                    // .addProfiler(ClassloaderProfiler.class)
                    // .addProfiler(CompilerProfiler.class)
                    .build();

            new Runner(opt).run();
        }
    }

    static sun.misc.Unsafe getUnsafe() {
        try {
            Field f = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            return (sun.misc.Unsafe)f.get(null);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    @State(Scope.Thread)
    @Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
    @Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
    @Fork(3)
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public static class HiddenClass {
        private static final MethodHandles.Lookup HOST_LOOKUP =
                defineHostClass(new Loader("hidden-class-loader"),"foo.AnonymousHost", FOO_HOST_BYTES);

        @Benchmark
        public Class<?> load() throws ClassNotFoundException {
            try {
                return HOST_LOOKUP.defineHiddenClass(X_BYTECODE, false, STRONG).lookupClass();
            } catch (IllegalAccessException e) {
                throw new InternalError(e);
            }
        }

        public static void main(String[] args) throws RunnerException {
            Options opt = new OptionsBuilder()
                    .include(LookupDefineClass.HiddenClass.class.getSimpleName())
                    // .addProfiler(ClassloaderProfiler.class)
                    // .addProfiler(CompilerProfiler.class)
                    .build();

            new Runner(opt).run();
        }
    }
}

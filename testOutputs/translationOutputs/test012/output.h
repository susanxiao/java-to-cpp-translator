#pragma once
#include <iostream>
#include "java_lang.h"

using namespace java::lang;

namespace inputs {
	namespace test012 {
		struct __A;
		struct __A_VT;

		struct __B1;
		struct __B1_VT;

		struct __B2;
		struct __B2_VT;

		struct __C;
		struct __C_VT;


		typedef __A* A;
		typedef __B1* B1;
		typedef __B2* B2;
		typedef __C* C;

		struct __A {
			__A_VT* __vptr;
			String a;

			__A();

			static Class __class();

			static __A_VT __vtable;

			static void methodSetA(A, String);
			static void methodPrintOther(A, A);
			static String methodMyToString(A);
		};

		struct __A_VT {
			Class __isa;

			int32_t (*hashCode)(A);
			bool (*equals)(A, Object);
			Class (*getClass)(A);
			String (*toString)(A);
			void (*methodSetA)(A, String);
			void (*methodPrintOther)(A, A);
			String (*methodMyToString)(A);

			__A_VT()
			: __isa(__A::__class()),
			hashCode((int32_t(*)(A))&__Object::hashCode),
			equals((bool(*)(A, Object))&__Object::equals),
			getClass((Class(*)(A))&__Object::getClass),
			toString((String(*)(A))&__Object::toString),
			methodSetA (&__A::methodSetA),
			methodPrintOther (&__A::methodPrintOther),
			methodMyToString (&__A::methodMyToString)
			{}
		};

		struct __B1 {
			__B1_VT* __vptr;
			String b;
			__A parent;

			__B1();

			static Class __class();

			static __B1_VT __vtable;

		};

		struct __B1_VT {
			Class __isa;

			int32_t (*hashCode)(B1);
			bool (*equals)(B1, Object);
			Class (*getClass)(B1);
			String (*toString)(B1);
			void (*methodSetA)(B1, String);
			void (*methodPrintOther)(B1, A);
			String (*methodMyToString)(B1);

			__B1_VT()
			: __isa(__B1::__class()),
			hashCode((int32_t(*)(B1))&__Object::hashCode),
			equals((bool(*)(B1, Object))&__Object::equals),
			getClass((Class(*)(B1))&__Object::getClass),
			toString((String(*)(B1))&__Object::toString),
			methodSetA((void(*)(B1, String))&__A::methodSetA),
			methodPrintOther((void(*)(B1, A))&__A::methodPrintOther),
			methodMyToString((String(*)(B1))&__A::methodMyToString)
			{}
		};

		struct __B2 {
			__B2_VT* __vptr;
			String b;
			__A parent;

			__B2();

			static Class __class();

			static __B2_VT __vtable;

		};

		struct __B2_VT {
			Class __isa;

			int32_t (*hashCode)(B2);
			bool (*equals)(B2, Object);
			Class (*getClass)(B2);
			String (*toString)(B2);
			void (*methodSetA)(B2, String);
			void (*methodPrintOther)(B2, A);
			String (*methodMyToString)(B2);

			__B2_VT()
			: __isa(__B2::__class()),
			hashCode((int32_t(*)(B2))&__Object::hashCode),
			equals((bool(*)(B2, Object))&__Object::equals),
			getClass((Class(*)(B2))&__Object::getClass),
			toString((String(*)(B2))&__Object::toString),
			methodSetA((void(*)(B2, String))&__A::methodSetA),
			methodPrintOther((void(*)(B2, A))&__A::methodPrintOther),
			methodMyToString((String(*)(B2))&__A::methodMyToString)
			{}
		};

		struct __C {
			__C_VT* __vptr;
			String c;
			__B1 parent;

			__C();

			static Class __class();

			static __C_VT __vtable;

			static String methodMyToString(C);
		};

		struct __C_VT {
			Class __isa;

			int32_t (*hashCode)(C);
			bool (*equals)(C, Object);
			Class (*getClass)(C);
			String (*toString)(C);
			void (*methodSetA)(C, String);
			void (*methodPrintOther)(C, A);
			String (*methodMyToString)(C);

			__C_VT()
			: __isa(__C::__class()),
			hashCode((int32_t(*)(C))&__Object::hashCode),
			equals((bool(*)(C, Object))&__Object::equals),
			getClass((Class(*)(C))&__Object::getClass),
			toString((String(*)(C))&__Object::toString),
			methodSetA((void(*)(C, String))&__A::methodSetA),
			methodPrintOther((void(*)(C, A))&__A::methodPrintOther),
			methodMyToString (&__C::methodMyToString)
			{}
		};

	}
}

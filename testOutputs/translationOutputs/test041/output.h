#pragma once
#include <iostream>
#include "java_lang.h"

using namespace java::lang;

namespace inputs {
	namespace test041 {
		struct __A;
		struct __A_VT;

		struct __B;
		struct __B_VT;

		struct __C;
		struct __C_VT;


		typedef __A* A;
		typedef __B* B;
		typedef __C* C;

		struct __A {
			__A_VT* __vptr;

			__A();

			static Class __class();

			static __A_VT __vtable;

			static void methodMObjectObject(A, Object, Object);
			static void methodMAObject(A, A, Object);
			static void methodMObjectA(A, Object, A);
		};

		struct __A_VT {
			Class __isa;

			int32_t (*hashCode)(A);
			bool (*equals)(A, Object);
			Class (*getClass)(A);
			String (*toString)(A);
			void (*methodMObjectObject)(A, Object, Object);
			void (*methodMAObject)(A, A, Object);
			void (*methodMObjectA)(A, Object, A);

			__A_VT()
			: __isa(__A::__class()),
			hashCode((int32_t(*)(A))&__Object::hashCode),
			equals((bool(*)(A, Object))&__Object::equals),
			getClass((Class(*)(A))&__Object::getClass),
			toString((String(*)(A))&__Object::toString),
			methodMObjectObject(&__A::methodMObjectObject),
			methodMAObject(&__A::methodMAObject),
			methodMObjectA(&__A::methodMObjectA)
			{}
		};

		struct __B {
			__B_VT* __vptr;
			__A parent;

			__B();

			static Class __class();

			static __B_VT __vtable;

			static void methodMObjectObject(B, Object, Object);
			static void methodMBObject(B, B, Object);
			static void methodMObjectB(B, Object, B);
		};

		struct __B_VT {
			Class __isa;

			int32_t (*hashCode)(B);
			bool (*equals)(B, Object);
			Class (*getClass)(B);
			String (*toString)(B);
			void (*methodMObjectObject)(B, Object, Object);
			void (*methodMAObject)(B, A, Object);
			void (*methodMObjectA)(B, Object, A);
			void (*methodMBObject)(B, B, Object);
			void (*methodMObjectB)(B, Object, B);

			__B_VT()
			: __isa(__B::__class()),
			hashCode((int32_t(*)(B))&__Object::hashCode),
			equals((bool(*)(B, Object))&__Object::equals),
			getClass((Class(*)(B))&__Object::getClass),
			toString((String(*)(B))&__Object::toString),
			methodMObjectObject(&__B::methodMObjectObject),
			methodMAObject((void(*)(B, A, Object))&__A::methodMAObject),
			methodMObjectA((void(*)(B, Object, A))&__A::methodMObjectA),
			methodMBObject(&__B::methodMBObject),
			methodMObjectB(&__B::methodMObjectB)
			{}
		};

		struct __C {
			__C_VT* __vptr;
			__A parent;

			__C();

			static Class __class();

			static __C_VT __vtable;

			static void methodMCC(C, C, C);
		};

		struct __C_VT {
			Class __isa;

			int32_t (*hashCode)(C);
			bool (*equals)(C, Object);
			Class (*getClass)(C);
			String (*toString)(C);
			void (*methodMObjectObject)(C, Object, Object);
			void (*methodMAObject)(C, A, Object);
			void (*methodMObjectA)(C, Object, A);
			void (*methodMCC)(C, C, C);

			__C_VT()
			: __isa(__C::__class()),
			hashCode((int32_t(*)(C))&__Object::hashCode),
			equals((bool(*)(C, Object))&__Object::equals),
			getClass((Class(*)(C))&__Object::getClass),
			toString((String(*)(C))&__Object::toString),
			methodMObjectObject((void(*)(C, Object, Object))&__A::methodMObjectObject),
			methodMAObject((void(*)(C, A, Object))&__A::methodMAObject),
			methodMObjectA((void(*)(C, Object, A))&__A::methodMObjectA),
			methodMCC(&__C::methodMCC)
			{}
		};

	}
}

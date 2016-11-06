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

			static void setA(A, String);
			static void printOther(A, A);
			static String myToString(A);
			static String toString(A);
		};

		struct __A_VT {
			Class __isa;

			bool (*equals)(A, Object);
			Class (*getClass)(A);
			int32_t (*hashCode)(A);
			String (*myToString)(A);
			void (*printOther)(A, A);
			void (*setA)(A, String);
			String (*toString)(A);

			__A_VT()
			: __isa(__A::__class()),
			equals((bool(*)(A, Object))&__Object::equals),
			getClass((Class(*)(A))&__Object::getClass),
			hashCode((int32_t(*)(A))&__Object::hashCode),
			myToString(&__A::myToString),
			printOther(&__A::printOther),
			setA(&__A::setA),
			toString(&__A::toString)
			{}
		};

		struct __B1 {
			__B1_VT* __vptr;
			String b;
			__A parent;

			__B1();

			static Class __class();

			static __B1_VT __vtable;

			static String toString(B1);
		};

		struct __B1_VT {
			Class __isa;

			bool (*equals)(B1, Object);
			Class (*getClass)(B1);
			int32_t (*hashCode)(B1);
			String (*myToString)(B1);
			void (*printOther)(B1, A);
			void (*setA)(B1, String);
			String (*toString)(B1);

			__B1_VT()
			: __isa(__B1::__class()),
			equals((bool(*)(B1, Object))&__Object::equals),
			getClass((Class(*)(B1))&__Object::getClass),
			hashCode((int32_t(*)(B1))&__Object::hashCode),
			myToString((String(*)(B1))&__A::myToString),
			printOther((void(*)(B1, A))&__A::printOther),
			setA((void(*)(B1, String))&__A::setA),
			toString(&__B1::toString)
			{}
		};

		struct __B2 {
			__B2_VT* __vptr;
			String b;
			__A parent;

			__B2();

			static Class __class();

			static __B2_VT __vtable;

			static String toString(B2);
		};

		struct __B2_VT {
			Class __isa;

			bool (*equals)(B2, Object);
			Class (*getClass)(B2);
			int32_t (*hashCode)(B2);
			String (*myToString)(B2);
			void (*printOther)(B2, A);
			void (*setA)(B2, String);
			String (*toString)(B2);

			__B2_VT()
			: __isa(__B2::__class()),
			equals((bool(*)(B2, Object))&__Object::equals),
			getClass((Class(*)(B2))&__Object::getClass),
			hashCode((int32_t(*)(B2))&__Object::hashCode),
			myToString((String(*)(B2))&__A::myToString),
			printOther((void(*)(B2, A))&__A::printOther),
			setA((void(*)(B2, String))&__A::setA),
			toString(&__B2::toString)
			{}
		};

		struct __C {
			__C_VT* __vptr;
			String c;
			__B1 parent;

			__C();

			static Class __class();

			static __C_VT __vtable;

			static String myToString(C);
			static String toString(C);
		};

		struct __C_VT {
			Class __isa;

			bool (*equals)(C, Object);
			Class (*getClass)(C);
			int32_t (*hashCode)(C);
			String (*myToString)(C);
			void (*printOther)(C, A);
			void (*setA)(C, String);
			String (*toString)(C);

			__C_VT()
			: __isa(__C::__class()),
			equals((bool(*)(C, Object))&__Object::equals),
			getClass((Class(*)(C))&__Object::getClass),
			hashCode((int32_t(*)(C))&__Object::hashCode),
			myToString(&__C::myToString),
			printOther((void(*)(C, A))&__A::printOther),
			setA((void(*)(C, String))&__A::setA),
			toString(&__C::toString)
			{}
		};

	}
}
